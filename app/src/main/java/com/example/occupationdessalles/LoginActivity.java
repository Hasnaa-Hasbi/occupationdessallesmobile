package com.example.occupationdessalles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.occupationdessalles.beans.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    boolean isUserNameValid, isPasswordValid;
    TextInputLayout userNameError, passError;
    List<User> users;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        userNameError = (TextInputLayout) findViewById(R.id.usernameError);
        passError = (TextInputLayout) findViewById(R.id.passError);

        extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("logout") != null){
                username.getText().clear();
                password.getText().clear();
                Toast.makeText(getApplicationContext(), "Logged out !", Toast.LENGTH_SHORT).show();

            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidation();
            }
        });
    }

    @Override
    public void onBackPressed() {
        username.getText().clear();
        password.getText().clear();
    }

    private void loginValidation() {

        if (username.getText().toString().isEmpty()) {
            userNameError.setError(getResources().getString(R.string.username_error));
            isUserNameValid = false;
        } else  {
            isUserNameValid = true;
            userNameError.setErrorEnabled(false);
        }

        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isUserNameValid && isPasswordValid) {

            final String usr = username.getText().toString().trim();
            final String paswd = password.getText().toString().trim();

            /*extras = getIntent().getExtras();
            if (extras != null) {
                users = (List<User>) extras.getSerializable("users");
            }*/

            boolean data=false;
            if ("admin".equals(usr) && "admin".equals(paswd)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Connecté !", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                data=true;
            }
            if(data==false){
                Toast.makeText(getApplicationContext(), "Identifiants invalides !", Toast.LENGTH_LONG).show();
            }


            /*for(User user : users){
                boolean data=false;
                if (String.valueOf(user.getUsername()).equals(usr) && String.valueOf(user.getPassword()).equals(paswd)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Connecté !", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    data=true;
                }
                if(data==false){
                    Toast.makeText(getApplicationContext(), "Identifiants invalides !", Toast.LENGTH_LONG).show();
                }
            }*/


        }

    }




}