package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
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

    private int previousSteps;

    private int actualSteps;

    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();

        executeRefresh(extras.get("email").toString(), extras.get("password").toString(),
                        extras.get("token").toString(), extras.get("token_refresh").toString());

        previousTime=java.lang.System.currentTimeMillis();
        previousSteps=0;

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
        Log.i("pasos iniciales", String.valueOf(Sensor.TYPE_STEP_COUNTER));
        if(countSensor != null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Sensor no encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onSensorChanged(SensorEvent event){
        switch(event.sensor.getType()){
            case Sensor.TYPE_STEP_COUNTER:
                if(running){
                    Long seconds;
                    actualSteps= (int) event.values[0];
                    Log.i("actual steps", String.valueOf(actualSteps));
                    Log.i("previous steps", String.valueOf(previousSteps));
                    tvSteps.setText(String.valueOf(event.values[0]));
                    actualTime=java.lang.System.currentTimeMillis();
                    seconds=(actualTime-previousTime)/1000;
                    if(seconds>=10){
                        Log.i("previous Time",previousTime.toString());
                        Log.i("actual Time",actualTime.toString());
                        Log.i("seconds",seconds.toString());
                        //Log.i("total Time",totalTime.toString());

                        tvSpeed.setText(seconds.toString());
                        tvSpeed.setText(String.valueOf((double)(actualSteps-previousSteps)/seconds)+"p/s");
                        Log.i("actual steps - previous steps", String.valueOf(actualSteps-previousSteps));
                        previousTime=actualTime;
                        previousSteps=actualSteps;
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


    private void executeRefresh(String ... strings){
        User user = new User(strings[0], strings[1], strings[2], strings[3]);
        user.refreshTokenTask();
    }
}