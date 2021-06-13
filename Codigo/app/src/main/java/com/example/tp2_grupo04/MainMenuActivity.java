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

import java.text.DecimalFormat;

import static java.lang.String.format;

public class MainMenuActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    private TextView tvSteps;
    private TextView tvSpeed;
    private TextView tvActiveTime;
    private TextView tvDistance;




    private Long initialTime;

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
        initialTime=java.lang.System.currentTimeMillis();
        previousSteps=0;
        actualSteps=0;
        tvSteps = (TextView) findViewById(R.id.textViewSteps2);
        tvSpeed = (TextView) findViewById(R.id.textViewSpeed2);
        tvActiveTime = (TextView) findViewById(R.id.textViewActiveTime2);
        tvDistance = (TextView) findViewById(R.id.textViewDistance2);


        tvSteps.setText("0");
        tvActiveTime.setText("0s");
        tvSpeed.setText("0p/s");
        tvDistance.setText("0m");

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
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepSensor != null){
            sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Sensor no encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        Log.i("debugeoSensorGetType", String.valueOf(event.sensor.getType()));
        Log.i("debugeoTypeStepDetector", String.valueOf(Sensor.TYPE_STEP_DETECTOR));
        switch(event.sensor.getType()){

            case Sensor.TYPE_STEP_DETECTOR:
                if(running){
                    Long seconds;
                    actualSteps++;
                    Log.i("actual steps", String.valueOf(actualSteps));
                    Log.i("previous steps", String.valueOf(previousSteps));
                    tvSteps.setText(String.valueOf(actualSteps));

                    tvDistance.setText(String.valueOf(new DecimalFormat("#.##").format(actualSteps*0.9))+"m");
                    actualTime=java.lang.System.currentTimeMillis();
                    seconds=(actualTime-previousTime)/1000;
                    tvActiveTime.setText(String.valueOf((actualTime-initialTime)/1000)+"s");

                    if(seconds>=5){
                        Log.i("previous Time",previousTime.toString());
                        Log.i("actual Time",actualTime.toString());
                        Log.i("seconds",seconds.toString());
                        //Log.i("total Time",totalTime.toString());
                        tvSpeed.setText(String.valueOf(new DecimalFormat("#.##").format((double)(actualSteps-previousSteps)/seconds))+"p/s");

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