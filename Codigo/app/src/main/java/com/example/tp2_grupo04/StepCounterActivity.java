package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    private TextView tvSteps;
    private TextView tvSpeed;
    private TextView tvActiveTime;
    private TextView tvDistance;
    private AlertDialog alertDialog;

    private SharedPreferences sp;
    public static final String ACTUAL_STEPS = "ActualSteps";
    public static final String INITIAL_TIME = "InitialTime";
    public static final String ACTIVE_TIME = "InitialTime";

    private Long initialTime;
    private Long previousTime;
    private Long actualTime;
    private Long activeTime;
    private Integer previousSteps;
    private Integer actualSteps;
    private Double speed;
    private String iSteps;
    private String iTime;
    private String iActive;
    boolean running = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        sp = this.getSharedPreferences(Utils.SP_STEP_TIME, Context.MODE_PRIVATE);
        iSteps = sp.getString(StepCounterActivity.ACTUAL_STEPS, "-1");
        iTime = sp.getString(StepCounterActivity.INITIAL_TIME, "-1");
        iActive = sp.getString(StepCounterActivity.ACTIVE_TIME, "-1");




        previousTime=java.lang.System.currentTimeMillis();
        previousSteps=0;

        tvSteps = (TextView) findViewById(R.id.textViewSteps2);
        tvSpeed = (TextView) findViewById(R.id.textViewSpeed2);
        tvActiveTime = (TextView) findViewById(R.id.textViewActiveTime2);
        tvDistance = (TextView) findViewById(R.id.textViewDistance2);

        if((!iSteps.matches("-1") && iSteps != null && !iSteps.matches("") )
                && (!iTime.matches("-1") && iTime != null  && !iTime.matches("") )
                && (!iActive.matches("-1") && iActive != null  && !iActive.matches("") )){
            initialTime = Long.valueOf(iTime);
            actualSteps = Integer.valueOf(iSteps);
            tvSteps.setText(String.valueOf(actualSteps));
            tvDistance.setText(String.valueOf(new DecimalFormat("#.##").format(actualSteps*0.9))+"m");
            tvActiveTime.setText(String.valueOf(iActive)+"s");

        }
        else{
            initialTime=java.lang.System.currentTimeMillis();
            actualSteps=0;
            tvSteps.setText("0");
            tvDistance.setText("0m");

        }
        tvActiveTime.setText("0s");
        tvSpeed.setText("0p/s");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        alertDialog = new AlertDialog.Builder(StepCounterActivity.this).create();
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
    protected void onPause(){
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
        //Log.i("debugeoSensorGetType", String.valueOf(event.sensor.getType()));
        //Log.i("debugeoTypeStepDetector", String.valueOf(Sensor.TYPE_STEP_DETECTOR));
        switch(event.sensor.getType()){

            case Sensor.TYPE_STEP_DETECTOR:
                if(running){
                    Long seconds;
                    actualSteps++;
                    //Log.i("actual steps", String.valueOf(actualSteps));
                    //Log.i("previous steps", String.valueOf(previousSteps));
                    tvSteps.setText(String.valueOf(actualSteps));

                    tvDistance.setText(String.valueOf(new DecimalFormat("#.##").format(actualSteps*0.9))+"m");
                    actualTime=java.lang.System.currentTimeMillis();
                    seconds=(actualTime-previousTime)/1000;
                    activeTime = (actualTime-initialTime)/1000;
                    tvActiveTime.setText(String.valueOf(activeTime)+"s");

                    if(seconds>=5){
                        //Log.i("previous Time",previousTime.toString());
                        //Log.i("actual Time",actualTime.toString());
                        //Log.i("seconds",seconds.toString());
                        //Log.i("total Time",totalTime.toString());
                        speed=(double)(actualSteps-previousSteps)/seconds;
                        Log.i("debug147 speed", String.valueOf(speed));
                        tvSpeed.setText(String.valueOf(new DecimalFormat("#.##").format(speed)+"p/s"));

                        //Log.i("actual st - previous st", String.valueOf(actualSteps-previousSteps));
                        previousTime=actualTime;
                        previousSteps=actualSteps;

                        if (speed>=2) {
                            setAlertText("Atención!", "Debe hacer reposo, no corra!");
                        }

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

    public void onClickRestart(View view) {
        tvSteps.setText("0");
        tvActiveTime.setText("0s");
        tvSpeed.setText("0p/s");
        tvDistance.setText("0m");
        previousTime=java.lang.System.currentTimeMillis();
        initialTime=java.lang.System.currentTimeMillis();
        previousSteps=0;
        actualSteps=0;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(StepCounterActivity.ACTUAL_STEPS, actualSteps.toString());
        editor.putString(StepCounterActivity.INITIAL_TIME, initialTime.toString());
        editor.putString(StepCounterActivity.ACTIVE_TIME, activeTime.toString());
        editor.commit();
    }


    //Falta que envie la info a Menu para que no se pierda ? Discutirlo
    public void onClickBack(View view) {
        Intent intent = new Intent(StepCounterActivity.this, MenuActivity.class);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(StepCounterActivity.ACTUAL_STEPS, actualSteps.toString());
        editor.putString(StepCounterActivity.INITIAL_TIME, initialTime.toString());
        if(activeTime != null) {
            editor.putString(StepCounterActivity.ACTIVE_TIME, activeTime.toString());
        }
        editor.commit();
        startActivity(intent);
        finish();
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
        final MediaPlayer alertSound = MediaPlayer.create(StepCounterActivity.this, R.raw.alertsound);
        alertSound.start();


    }

}