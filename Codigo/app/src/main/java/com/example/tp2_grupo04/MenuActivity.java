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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    public SensorManager sensorManager;
    private TextView etiqLocation;
    private TextView etiqProximity;
    private AlertDialog alertDialog;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sp;
    private SharedPreferences spProximity;
    public Double lat = 1.0;
    public Double lon = 1.0;
    private String iSteps;
    private String iTime;

    public boolean isCloseDistance() {
        return closeDistance;
    }

    private boolean closeDistance;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AppInfo", "<<<<ON_CREATE MENU_ACTIVITY>>>>");
        setContentView(R.layout.activity_menu);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        etiqLocation = (TextView) findViewById(R.id.etiqLocationMenu);
        etiqProximity = (TextView) findViewById(R.id.etiqProximity);
        alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        sp = this.getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
        spProximity = this.getSharedPreferences(Utils.PROXIMITY_SENSOR, Context.MODE_PRIVATE);
        String proximitySP = spProximity.getString("Proximity", null);
        String latitudeSP = sp.getString("Latitude", null);
        String longitudeSP = sp.getString("Longitude", null);
        if (latitudeSP != null && longitudeSP != null) {
            etiqLocation.setText("Ultima ubicación: " + latitudeSP + ", " + longitudeSP);
            if (etiqLocation.getVisibility() == View.GONE) {
                etiqLocation.setVisibility(View.VISIBLE);
            }
        } else {
            etiqLocation.setVisibility(View.GONE);
        }
        if (proximitySP != null ) {
            etiqProximity.setText("Sensor Proximidad: " + proximitySP );
            if (etiqProximity.getVisibility() == View.GONE) {
                etiqProximity.setVisibility(View.VISIBLE);
            }
        } else {
            etiqProximity.setVisibility(View.GONE);
        }
        getLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppInfo", "<<<<ON_STOP MENU_ACTIVITY>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AppInfo", "<<<<ON_DESTROY MENU_ACTIVITY>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AppInfo", "<<<<ON_PAUSE MENU_ACTIVITY>>>>");
        sensorManager.unregisterListener(MenuActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppInfo", "<<<<ON_RESUME MENU_ACTIVITY>>>>");
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor no encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("AppInfo", "<<<<ON_START MENU_ACTIVITY>>>>");
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
        startActivity(intent);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY:
                String proximitySP = String.valueOf(event.values[0]);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("Proximity");
                editor.putString("Proximity", proximitySP);
                editor.commit();
                etiqProximity.setText("Sensor Proximidad: " + proximitySP);
                if (etiqProximity.getVisibility() == View.GONE) {
                    etiqProximity.setVisibility(View.VISIBLE);
                }

                if (event.values[0] < event.sensor.getMaximumRange()) {
                    this.closeDistance = true;
                    new DistanceSensorAsyncTask(MenuActivity.this).execute();
                } else {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {

        if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        MenuActivity.this.lat = location.getLatitude();
                        MenuActivity.this.lon = location.getLongitude();
                        SharedPreferences.Editor editor = MenuActivity.this.sp.edit();
                        String latitudeSP =  MenuActivity.this.sp.getString("Latitude", null);
                        String longitudeSP =  MenuActivity.this.sp.getString("Longitude", null);
                        Log.i("Debug191", "Originalmente tengo: " + latitudeSP + " , " + longitudeSP);
                        editor.remove("Latitude");
                        editor.remove("Longitude");
                        latitudeSP =  MenuActivity.this.sp.getString("Latitude", null);
                        longitudeSP =  MenuActivity.this.sp.getString("Longitude", null);
                        Log.i("Debug196", "Dps de borrar tengo: " + latitudeSP + " , " + longitudeSP);

                        editor.putString("Latitude", MenuActivity.this.lat.toString());
                        editor.putString("Longitude", MenuActivity.this.lon.toString());
                        latitudeSP = MenuActivity.this.lat.toString();
                        longitudeSP = MenuActivity.this.lon.toString();
                        Log.i("Debug202", "Al final tengo: " + latitudeSP + " , " + longitudeSP);
                        etiqLocation.setText("Ultima ubicación: " + latitudeSP + ", " + longitudeSP);
                        if (etiqLocation.getVisibility() == View.GONE) {
                            etiqLocation.setVisibility(View.VISIBLE);
                        }
                        editor.commit();
                    }
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            getLocation();
        }
    }


    public void setAlertText(String title, String message) {
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