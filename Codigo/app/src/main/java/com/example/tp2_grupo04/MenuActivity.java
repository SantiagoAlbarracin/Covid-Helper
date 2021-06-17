package com.example.tp2_grupo04;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    private Long initialTime;
    private Long previousTime;
    private Long actualTime;
    private int previousSteps;
    private int actualSteps;
    private TextView etiqLocation;
    private AlertDialog alertDialog;

    public static final String USER_EMAIL = "login_email";
    public static final String USER_PASSWORD = "login_password";
    public static final String USER_TOKEN = "login_token";
    public static final String USER_TOKEN_REFRESH = "login_token_refresh";

    private String userEmail;
    private String userPassword;
    private String userToken;
    private String userTokenRefresh;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sp;
    public Double lat = 1.0;
    public Double lon = 1.0;


    public boolean isCloseDistance() {
        return closeDistance;
    }

    private boolean closeDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        etiqLocation = (TextView) findViewById(R.id.etiqLocationMenu);

        alertDialog = new AlertDialog.Builder(MenuActivity.this).create();

        sendEvent();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        sp = this.getSharedPreferences("UserLocation", Context.MODE_PRIVATE);

        String latitudeSP = sp.getString("Latitude", null);
        String longitudeSP = sp.getString("Longitude", null);

        if(latitudeSP != null && longitudeSP != null){
            etiqLocation.setText("Ultima ubicación: " + latitudeSP + ", " + longitudeSP);
            etiqLocation.setVisibility(View.VISIBLE);
        }
        else{
            etiqLocation.setVisibility(View.GONE);
        }
        getLocation();

    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor != null){
            sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Sensor no encontrado!", Toast.LENGTH_SHORT).show();
        }
    }




    public void sendEvent(){
        Intent intentEvent = getIntent();
        Bundle extras = intentEvent.getExtras();

        userEmail = extras.getString(USER_EMAIL);
        userPassword = extras.getString(USER_PASSWORD);
        userToken = extras.getString(USER_TOKEN);
        userTokenRefresh = extras.getString(USER_TOKEN_REFRESH);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();

        String eventDescription = "User Login " + userEmail + " at " + formatter.format(date).toString();

        new EventAsyncTask(MenuActivity.this).execute(Utils.TYPE_EVENT, eventDescription, userToken);
    }

    public void onClickStepCounter(View view) {
        Intent intent = new Intent(MenuActivity.this, StepCounterActivity.class);

        startActivity(intent);
        finish();
    }

    public void onClickDiagnosis(View view) {
        Intent intent = new Intent(MenuActivity.this, DiagnosisActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickLogOut(View view) {
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                if(event.values[0]<event.sensor.getMaximumRange()){
                    this.closeDistance = true;
                    Log.i("debug104","EL sensor detecta algo");
                    new DistanceSensorAsyncTask(MenuActivity.this).execute();
                }else{
                    this.closeDistance = false;
                    Log.i("debug108","EL sensor no detecta nada");
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void lanzarActivityDiagnosis(String... strings) {
        Intent intent = new Intent(MenuActivity.this, DiagnosisActivity.class);
        startActivity(intent);
        finish();
    }




    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            MenuActivity.this.lat = location.getLatitude();
                            MenuActivity.this.lon = location.getLongitude();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Latitude", MenuActivity.this.lat.toString());
                            editor.putString("Longitude", MenuActivity.this.lon.toString());
                            editor.commit();
                            Log.i("debug999", "Lat: " + MenuActivity.this.lat.toString() + " Lon: " + MenuActivity.this.lon.toString());
                        }
                    }
                });
            }else
            {
                Log.i("debug189","No tengo permiso de GPS");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                getLocation();
            }
        }
    }

    public void setAlertText(String title, String message){
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}