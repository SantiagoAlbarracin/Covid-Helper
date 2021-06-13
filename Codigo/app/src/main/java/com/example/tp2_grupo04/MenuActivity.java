package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private Long initialTime;
    private Long previousTime;
    private Long actualTime;
    private int previousSteps;
    private int actualSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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




}