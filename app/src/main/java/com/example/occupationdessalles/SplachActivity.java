package com.example.occupationdessalles;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import com.example.occupationdessalles.beans.User;
import com.example.occupationdessalles.network.RetrofitInstance;
import com.example.occupationdessalles.services.DataService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplachActivity extends AppCompatActivity {
    List<User> users = new ArrayList<>();
    ImageView image;
    DataService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.img);
        service = RetrofitInstance.getInstance().create(DataService.class);

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(1000);
        animation1.setFillAfter(true);
        image.startAnimation(animation1);

        Call<List<User>> request2=service.getUsers();
        request2.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users = response.body();
                System.out.println(users);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });

        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent  = new Intent(SplachActivity.this, LoginActivity.class);
                    intent.putExtra("users", (Serializable) users);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();


    }




}
