package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import javax.net.ssl.HostnameVerifier;

public class HospitalActivity extends AppCompatActivity {
    private String HospitalName;
    private String HospitalAddress;
    private String HospitalTelephone;
    private String HospitalDistance;
    private Boolean riskFactor;
    private TextView tvHospName;
    private TextView tvHospAddress;
    private TextView tvHospTel;
    private TextView tvHospDistance;
    private TextView tvHospCovid;
    private Float distance;

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
        riskFactor= extras.getBoolean("RiskFactor");


        tvHospName=(TextView)findViewById(R.id.textViewHospName2);
        tvHospAddress=(TextView)findViewById(R.id.textViewHospAddress2);
        tvHospTel=(TextView)findViewById(R.id.textViewHospTel2);
        tvHospDistance=(TextView)findViewById(R.id.textViewHospDistance2);
        tvHospCovid=(TextView)findViewById(R.id.textViewHospCovid);

        tvHospName.setText(HospitalName);
        tvHospAddress.setText(HospitalAddress);
        tvHospTel.setText(HospitalTelephone);
        distance=Float.parseFloat(HospitalDistance);
        tvHospDistance.setText(String.valueOf(new DecimalFormat("#.##").format(distance))+"km");

        String covidString = "Es probable que usted tenga COVID, evite el contacto estrecho con otras personas y use tapabocas.";

        if(riskFactor){
            covidString = covidString +" Debe tener especial cuidado ya que presenta factores de riesgo";
        }else{
            tvHospCovid.setText("Es probable que usted tenga COVID, evite el contacto estrecho con otras personas y use tapabocas.");
        }

        tvHospCovid.setText(covidString);


    }

    public void onClickBack(View view) {
        Intent intent = new Intent(HospitalActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}