package com.example.occupationdessalles;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.occupationdessalles.beans.Creneau;
import com.example.occupationdessalles.beans.Occupation;
import com.example.occupationdessalles.beans.Salle;
import com.example.occupationdessalles.network.RetrofitInstance;
import com.example.occupationdessalles.services.DataService;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private String idSalle="";
    private Salle salle;
    private Creneau creneau;
    private Occupation occupation;
    private List<Occupation> occupations;
    private String creneau_id;
    TextView nom_bloc;
    TextView bloc;
    TextView nom_salle;
    TextView type_salle;
    TextView nom_creneau;
    Button confirmer;
    DataService service;
    Date date;
    DateFormat format;
    DateFormat dateFormat;
    String dateString;
    String a;
    String time;
    double d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bloc = findViewById(R.id.textView2);
        nom_bloc=findViewById(R.id.bloc);
        nom_salle=findViewById(R.id.salle);
        type_salle=findViewById(R.id.type);
        nom_creneau=findViewById(R.id.creneau);
        confirmer=findViewById(R.id.confirmer);
        confirmer.setEnabled(false);
        service = RetrofitInstance.getInstance().create(DataService.class);
        occupations = getOccupations();
        if(checkPermission()){
            //Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
        }else{
            requestPermissions();
        }
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        //////////////////////
                        date = new Date();
                        dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
                        dateString = dateFormat.format(date);
                        format = new SimpleDateFormat("HH:mm");
                        time=format.format(date);

                        if(time.split(":")[1].split("")[0].equals("0")){
                            a=Integer.parseInt(time.split(":")[0])+".0"+Integer.parseInt(time.split(":")[1]);
                        }else{
                            a=Integer.parseInt(time.split(":")[0])+"."+Integer.parseInt(time.split(":")[1]);
                        }

                        d=Double.parseDouble(a);
                        //////////////////////
                        if(d>=8.3 && d<=10.2){
                            creneau_id="61c21b8d2b4b70c9193a1724";
                            getSalle(result);
                            getCreneau(creneau_id);
                        }else if(d>=10.3 && d<=12.2){
                            creneau_id="61c21bcc2b4b70c9193a1725";
                            getSalle(result);
                            getCreneau(creneau_id);
                        }else if(d>=13.3 && d<=15.2){
                            creneau_id="61c21c0d2b4b70c9193a1726";
                            getSalle(result);
                            getCreneau(creneau_id);
                        }else if(d>=15.3 && d<=17.2){
                            creneau_id="61c21c332b4b70c9193a1727";
                            getSalle(result);
                            getCreneau(creneau_id);
                        }else{
                            Toast.makeText(getApplicationContext(), "Vous ne pouvez pas réserver une salle dans ce créneau", Toast.LENGTH_LONG).show();
                            bloc.setText("");
                            nom_bloc.setText("");
                            nom_salle.setText("");
                            type_salle.setText("");
                            nom_creneau.setText("");
                            confirmer.setEnabled(false);
                        }
                        occupations = getOccupations();
                        //////////////////////

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloc.setText("");
                nom_bloc.setText(null);
                nom_salle.setText(null);
                type_salle.setText(null);
                nom_creneau.setText(null);
                confirmer.setEnabled(false);
                if(checkAvailability()){
                    occupation =new Occupation(salle.getId(), creneau_id);
                    createOccupation(occupation);
                    Toast.makeText(getApplicationContext(), "Salle réservée avec succès", Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Salle déjà occupée dans ce créneau !")
                            .setMessage("Voulez-vous libérer cette salle maintenant ?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteOccupation(occupation.getId());
                                    Toast.makeText(MainActivity.this, "Salle libérée !", Toast.LENGTH_LONG).show();
                                }})
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });
    }
    public void getSalle(@NonNull final Result r){
        Call<Salle> request=service.getSalle(r.getText());
        request.enqueue(new Callback<Salle>() {
            @Override
            public void onResponse(Call<Salle> call, Response<Salle> response) {
                if(!response.isSuccessful()){
                    //Toast.makeText(getApplicationContext(), "Code : "+response.code(), Toast.LENGTH_SHORT).show();
                    return ;
                }
                salle =response.body();
                bloc.setText("Bloc");
                nom_bloc.setText(salle.getNameBloc());
                nom_salle.setText(salle.getNom());
                type_salle.setText(salle.getType());
            }

            @Override
            public void onFailure(Call<Salle> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getCreneau(String c){
        Call<Creneau> request2=service.getCreneau(c);
        request2.enqueue(new Callback<Creneau>() {
            @Override
            public void onResponse(Call<Creneau> call, Response<Creneau> response) {
                creneau=response.body();
                nom_creneau.setText(creneau.getHeure_debut()+"-"+creneau.getHeure_fin());
                confirmer.setEnabled(true);
            }
            @Override
            public void onFailure(Call<Creneau> call, Throwable t) {
            }
        });
    }

    public void deleteOccupation(String id){
        Call<Occupation> request2=service.deleteOccupation(id);
        request2.enqueue(new Callback<Occupation>() {
            @Override
            public void onResponse(Call<Occupation> call, Response<Occupation> response) {
                Call<List<Occupation>> request2=service.getOccupations();
                request2.enqueue(new Callback<List<Occupation>>() {
                    @Override
                    public void onResponse(Call<List<Occupation>> call2, Response<List<Occupation>> response2) {
                        occupations = response2.body();
                    }
                    @Override
                    public void onFailure(Call<List<Occupation>> call, Throwable t) {
                    }
                });
            }
            @Override
            public void onFailure(Call<Occupation> call, Throwable t) {
            }
        });
    }

    public List<Occupation> getOccupations(){
        Call<List<Occupation>> request2=service.getOccupations();
        request2.enqueue(new Callback<List<Occupation>>() {
            @Override
            public void onResponse(Call<List<Occupation>> call, Response<List<Occupation>> response) {
                occupations = response.body();
                System.out.println(occupations);
            }
            @Override
            public void onFailure(Call<List<Occupation>> call, Throwable t) {
            }
        });
        return occupations;
    }
    public void createOccupation(Occupation occupation){
        Call<Occupation> request3 =service.createOccupation(occupation);
        request3.enqueue(new Callback<Occupation>() {
            @Override
            public void onResponse(Call<Occupation> call, Response<Occupation> response) {
                Call<List<Occupation>> request2=service.getOccupations();
                request2.enqueue(new Callback<List<Occupation>>() {
                    @Override
                    public void onResponse(Call<List<Occupation>> call2, Response<List<Occupation>> response2) {
                        occupations = response2.body();
                    }
                    @Override
                    public void onFailure(Call<List<Occupation>> call, Throwable t) {
                    }
                });
            }

            @Override
            public void onFailure(Call<Occupation> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean checkAvailability(){
        occupations = getOccupations();
        for(Occupation occup:occupations){
            if(occup.getSalle().equals(salle.getId()) &&  occup.getCreneau().equals(creneau_id) && occup.getDate().equals(dateString)){
                occupation =new Occupation(salle.getId(), creneau_id);
                occupation.setId(occup.getId());
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    private boolean checkPermission(){
        int camera_permission= ContextCompat.checkSelfPermission(this,CAMERA);
        return camera_permission== PackageManager.PERMISSION_GRANTED;
    }
    public void requestPermissions(){
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    public void logout(MenuItem item) {
        String logout = "logout";
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("logout", logout);
        startActivity(intent);
        finish();
    }

}