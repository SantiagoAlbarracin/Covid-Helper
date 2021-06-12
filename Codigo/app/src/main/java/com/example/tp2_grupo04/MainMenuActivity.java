package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    private TextView tvSteps;
    private TextView tvSpeed;



    private Long previousTime;

    private Long actualTime;

    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tvSteps = (TextView) findViewById(R.id.textViewSteps2);
        tvSpeed = (TextView) findViewById(R.id.textViewSpeed2);

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
        running = false;
        //Para que el hardware deje de detectar pasos
        //sensorManager.unregisterListener(this);
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        previousTime = java.lang.System.currentTimeMillis();
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Sensor no encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(running){
            tvSteps.setText(String.valueOf(event.values[0]));
            actualTime=java.lang.System.currentTimeMillis()-previousTime;
            tvSpeed.setText(actualTime.toString());
            previousTime=actualTime;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}