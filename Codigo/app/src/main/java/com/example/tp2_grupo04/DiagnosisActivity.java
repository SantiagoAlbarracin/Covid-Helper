package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TreeMap;


public class DiagnosisActivity extends AppCompatActivity {

    private RadioButton btnTempHigh;
    private RadioButton btnTempLow;
    private RadioButton btnYesHeadMuscleAche;
    private RadioButton btnNoHeadMuscleAche;
    private RadioButton btnYesCoughSoreThroat;
    private RadioButton btnNoCoughSoreThroat;
    private RadioButton btnYesRespiratoryDistress;
    private RadioButton btnNoRespiratoryDistress;
    private RadioButton btnYesSmellTaste;
    private RadioButton btnNoSmellTaste;
    private CheckBox cbDiabetes;
    private CheckBox cbLowDefence;
    private CheckBox cbHeartDisease;
    private CheckBox cbRespiratoryDisease;
    private Button btnCancel;
    private Button btnSend;
    private AlertDialog alertDialog;
    private SharedPreferences sp;
    private HashMap<Integer, Hospital> hospitalsArray;
    private TreeMap<Double, Hospital> distancesArray;
    private Double lat = 1.0;
    private Double lon = 1.0;
    private String latitudeSP;
    private String longitudeSP;
    private LocationManager manager;
    private AlertDialog alert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AppInfo", "<<<<ON_CREATE DIAGNOSIS_ACTIVITY>>>>");
        setContentView(R.layout.activity_diagnosis);
        btnTempHigh = (RadioButton) findViewById(R.id.radioButton4);
        btnTempLow = (RadioButton) findViewById(R.id.radioButton2);
        btnYesHeadMuscleAche = (RadioButton) findViewById(R.id.radioButton11);
        btnNoHeadMuscleAche = (RadioButton) findViewById(R.id.radioButton_p2_r1);
        btnYesCoughSoreThroat = (RadioButton) findViewById(R.id.radioButton13);
        btnNoCoughSoreThroat = (RadioButton) findViewById(R.id.radioButton10);
        btnYesRespiratoryDistress = (RadioButton) findViewById(R.id.radioButton9);
        btnNoRespiratoryDistress = (RadioButton) findViewById(R.id.radioButton12);
        btnYesSmellTaste = (RadioButton) findViewById(R.id.radioButton_15);
        btnNoSmellTaste = (RadioButton) findViewById(R.id.radioButton8);
        cbDiabetes = (CheckBox) findViewById(R.id.checkBox3);
        cbLowDefence = (CheckBox) findViewById(R.id.checkBox5);
        cbHeartDisease = (CheckBox) findViewById(R.id.checkBox2);
        cbRespiratoryDisease = (CheckBox) findViewById(R.id.checkBox);
        btnCancel = (Button) findViewById(R.id.btnDiagCancel);
        btnSend = (Button) findViewById(R.id.btnDiagSend);
        alertDialog = new AlertDialog.Builder(DiagnosisActivity.this).create();
        sp = this.getSharedPreferences("UserLocation", Context.MODE_PRIVATE);
        latitudeSP="-34.6705306";
        longitudeSP="-58.5650036";
        if(sp.contains("Latitude") && sp.contains("Longitude")){
            latitudeSP = sp.getString("Latitude", null);
            longitudeSP = sp.getString("Longitude", null);
        }
        generateHospitals();
        calculateDistances(Double.valueOf(longitudeSP), Double.valueOf(latitudeSP));
    }

    private boolean allRadiosChecked() {
        if (!btnTempHigh.isChecked() && !btnTempLow.isChecked()) {
            return false;
        }
        if (!btnYesHeadMuscleAche.isChecked() && !btnNoHeadMuscleAche.isChecked()) {
            return false;
        }
        if (!btnYesCoughSoreThroat.isChecked() && !btnNoCoughSoreThroat.isChecked()) {
            return false;
        }
        if (!btnYesRespiratoryDistress.isChecked() && !btnNoRespiratoryDistress.isChecked()) {
            return false;
        }
        if (!btnYesSmellTaste.isChecked() && !btnNoSmellTaste.isChecked()) {
            return false;
        }
        return true;
    }

    private boolean hasRiskFactor() {
        if (cbDiabetes.isChecked() || cbLowDefence.isChecked() || cbHeartDisease.isChecked() ||
                cbRespiratoryDisease.isChecked()) {
            return true;
        }
        return false;
    }

    private boolean hasCovid() {
        if (btnTempHigh.isChecked() || btnYesHeadMuscleAche.isChecked() || btnYesCoughSoreThroat.isChecked() ||
                btnYesRespiratoryDistress.isChecked() || btnYesSmellTaste.isChecked()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DiagnosisActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickCancel(View view) {
        Intent intent = new Intent(DiagnosisActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    //Aca informamos hospital mas cercano si tiene covid
    public void onClickSend(View view) {
        if (!allRadiosChecked()) {
            setAlertText("¡Error!", "Por favor complete todos los campos");
        } else if (hasCovid()) {
            lanzarActivity();
        } else {
            setAlertTextMenuButton("Notificacion", "Usted no presenta sintomas de Covid 19");
        }
    }

    public void lanzarActivity(String... strings) {
        Intent intent = new Intent(DiagnosisActivity.this, HospitalActivity.class);
        Hospital hospital = distancesArray.firstEntry().getValue();
        intent.putExtra(Hospital.TAG_NAME_HOSPITAL, hospital.getName());
        intent.putExtra(Hospital.TAG_DISTANCE_HOSPITAL, distancesArray.firstEntry().getKey().toString());
        intent.putExtra(Hospital.TAG_TELEPHONE_HOSPITAL, hospital.getTelephone().toString());
        intent.putExtra(Hospital.TAG_ADDRESS_HOSPITAL, hospital.getAddress());
        boolean riskFactor = hasRiskFactor();
        intent.putExtra("RiskFactor", riskFactor);
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

    }

    public void setAlertTextMenuButton(String title, String message) {
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(DiagnosisActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void calculateDistances(Double lat, Double lon) {
        distancesArray = new TreeMap<Double, Hospital>();
        for (HashMap.Entry<Integer, Hospital> entry : hospitalsArray.entrySet()) {
            Integer key = entry.getKey();
            Hospital value = entry.getValue();
            Double distancia = (double) distance(value.getLatitude(), value.getLongitude(), lat, lon);
            distancesArray.put(distancia, value);
        }
    }

    public void generateHospitals() {
        JSONObject obj = null;
        Hospital hospital;
        hospitalsArray = new HashMap<Integer, Hospital>();
        try {
            obj = new JSONObject(readFile());
            JSONArray m_jArry = obj.getJSONArray("properties");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                JSONObject hsptProperties = jo_inside.getJSONObject("properties");
                String name = hsptProperties.getString("NOMBRE");
                String name_map = hsptProperties.getString("NOM_MAP");
                String address = hsptProperties.getString("DOM_NORMA");
                String telephone = hsptProperties.getString("TELEFONO");
                JSONObject geometryArray = jo_inside.getJSONObject("geometry");
                String coordinates = geometryArray.getString("coordinates");
                hospital = new Hospital(name, address, telephone, coordinates);
                hospitalsArray.put(i, hospital);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("HospitalsBA");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppInfo", "<<<<ON_STOP DIAGNOSIS_ACTIVITY>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AppInfo", "<<<<ON_DESTROY DIAGNOSIS_ACTIVITY>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppInfo", "<<<<ON_RESUME DIAGNOSIS_ACTIVITY>>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        Log.i("AppInfo", "<<<<ON_START DIAGNOSIS_ACTIVITY>>>>");
    }

    public double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRad = 6371;//en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distance = earthRad * va2;
        return distance;
    }

    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su GPS parece estar deshabilitado. Habilite el GPS.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        alert = builder.create();
        alert.show();
    }
}