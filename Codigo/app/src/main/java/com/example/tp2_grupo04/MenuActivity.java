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

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    private TextView etiqLocation;
    private AlertDialog alertDialog;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sp;

    public Double lat = 1.0;
    public Double lon = 1.0;
    private String iSteps;
    private String iTime;

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
    protected void onResume() {
        super.onResume();
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor != null){
            sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_UI);

        }else{
            Toast.makeText(this, "Sensor no encontrado!", Toast.LENGTH_SHORT).show();
        }
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
        TimerTaskClass ttc = new TimerTaskClass();
        ttc.getInstance().stopTimer();
        Log.i("debug777", "Hizo Logout");
        startActivity(intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                if(event.values[0]<event.sensor.getMaximumRange()){
                    this.closeDistance = true;
                    new DistanceSensorAsyncTask(MenuActivity.this).execute();
                }else{
                    this.closeDistance = false;
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
        sensorManager.unregisterListener(MenuActivity.this);
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
                           //Log.i("debug999", "Lat: " + MenuActivity.this.lat.toString() + " Lon: " + MenuActivity.this.lon.toString());
                        }
                    }
                });
            }else
            {
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