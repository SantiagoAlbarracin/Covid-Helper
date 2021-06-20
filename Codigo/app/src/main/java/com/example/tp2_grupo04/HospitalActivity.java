package com.example.tp2_grupo04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

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

    /*
        Se crea la activity Hospital, se setean etiquetas con informacion del hospital mas cercano y botones.
        Se le dan recomendaciones al usuario y en caso de que sea de riesgo, se le informará.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AppInfo", "<<<<ON_CREATE HOSPITAL_ACTIVITY>>>>");
        setContentView(R.layout.activity_hospital);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        HospitalName = extras.getString(Hospital.TAG_NAME_HOSPITAL);
        HospitalAddress = extras.getString(Hospital.TAG_ADDRESS_HOSPITAL);
        HospitalTelephone = extras.getString(Hospital.TAG_TELEPHONE_HOSPITAL);
        HospitalDistance = extras.getString(Hospital.TAG_DISTANCE_HOSPITAL);
        riskFactor = extras.getBoolean("RiskFactor");
        tvHospName = (TextView) findViewById(R.id.textViewHospName2);
        tvHospAddress = (TextView) findViewById(R.id.textViewHospAddress2);
        tvHospTel = (TextView) findViewById(R.id.textViewHospTel2);
        tvHospDistance = (TextView) findViewById(R.id.textViewHospDistance2);
        tvHospCovid = (TextView) findViewById(R.id.textViewHospCovid);
        tvHospName.setText(HospitalName);
        tvHospAddress.setText(HospitalAddress);
        tvHospTel.setText(HospitalTelephone);
        distance = Float.parseFloat(HospitalDistance);
        tvHospDistance.setText(String.valueOf(new DecimalFormat("#.##").format(distance)) + "km");
        String covidString = "Es probable que usted tenga COVID, evite el contacto estrecho con otras personas y use tapabocas.";
        if (riskFactor) {
            covidString = covidString + " Debe tener especial cuidado ya que presenta factores de riesgo";
        } else {
            tvHospCovid.setText("Es probable que usted tenga COVID, evite el contacto estrecho con otras personas y use tapabocas.");
        }
        tvHospCovid.setText(covidString);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppInfo", "<<<<ON_STOP HOSPITAL_ACTIVITY>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AppInfo", "<<<<ON_DESTROY HOSPITAL_ACTIVITY>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AppInfo", "<<<<ON_PAUSE HOSPITAL_ACTIVITY>>>>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppInfo", "<<<<ON_RESUME HOSPITAL_ACTIVITY>>>>");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("AppInfo", "<<<<ON_START HOSPITAL_ACTIVITY>>>>");
    }

    /*
        Al presionar el boton Aceptar, se dirige al usuario a la activity Menu.
     */
    public void onClickAccept(View view) {
        Intent intent = new Intent(HospitalActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    /*
        Cuando el usuario presiona el boton back de la Android UI, se lo dirige a la activity Login.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}