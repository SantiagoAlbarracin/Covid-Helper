package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
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

        previousTime=java.lang.System.currentTimeMillis();

        tvSteps = (TextView) findViewById(R.id.textViewSteps2);
        tvSpeed = (TextView) findViewById(R.id.textViewSpeed2);

        tvSpeed.setText("0");

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
        //running = false;
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

        super.onResume();
        previousTime = java.lang.System.currentTimeMillis();
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
        switch(event.sensor.getType()){
            case Sensor.TYPE_STEP_COUNTER:
                if(running){
                    Long seconds;
                    tvSteps.setText(String.valueOf(event.values[0]));
                    actualTime=java.lang.System.currentTimeMillis();
                    seconds=(actualTime-previousTime)/1000;
                    if(seconds>=5){
                        Log.i("previous Time",previousTime.toString());
                        Log.i("actual Time",actualTime.toString());
                        Log.i("seconds",seconds.toString());
                        //Log.i("total Time",totalTime.toString());
                        tvSpeed.setText(seconds.toString());
                        previousTime=actualTime;
                    }

                }
                break;
            default:
                break;

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}