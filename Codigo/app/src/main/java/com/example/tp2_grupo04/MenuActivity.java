package com.example.tp2_grupo04;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    private Long initialTime;
    private Long previousTime;
    private Long actualTime;
    private int previousSteps;
    private int actualSteps;

    public boolean isCloseDistance() {
        return closeDistance;
    }

    private boolean closeDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
}