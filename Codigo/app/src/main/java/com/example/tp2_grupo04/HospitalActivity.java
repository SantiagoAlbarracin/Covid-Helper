package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import javax.net.ssl.HostnameVerifier;

public class HospitalActivity extends AppCompatActivity {
    private String HospitalName;
    private String HospitalAddress;
    private String HospitalTelephone;
    private String HospitalDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        HospitalName= extras.getString(Hospital.TAG_NAME_HOSPITAL);
        HospitalAddress= extras.getString(Hospital.TAG_ADDRESS_HOSPITAL);
        HospitalTelephone= extras.getString(Hospital.TAG_TELEPHONE_HOSPITAL);
        HospitalDistance= extras.getString(Hospital.TAG_DISTANCE_HOSPITAL);

        

    }
}