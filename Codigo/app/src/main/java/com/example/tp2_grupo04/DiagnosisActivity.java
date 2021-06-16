package com.example.tp2_grupo04;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TreeMap;


public class DiagnosisActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    private HashMap<Integer, Hospital> hospitalsArray;
    private TreeMap<Double, Hospital> distancesArray;

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





    private FusedLocationProviderClient fusedLocationProviderClient;

    public Double lat = 1.0;
    public Double lon = 1.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        generateHospitals();

        getLocation();

    }

    private void calculateDistances(Double lat, Double lon){

        distancesArray = new TreeMap<Double, Hospital>();

        for (HashMap.Entry<Integer, Hospital> entry : hospitalsArray.entrySet()) {
            Integer key = entry.getKey();
            Hospital value = entry.getValue();

            Double distancia = (double) distance(value.getLatitude(),value.getLongitude(), lat, lon);

            distancesArray.put(distancia, value);

            //Log.i("debug38", "Distancia al " + value.getName() + " " + distancia.toString());
        }

    }

    private void getLocation(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
                if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){


                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                DiagnosisActivity.this.lat = location.getLatitude();
                                DiagnosisActivity.this.lon = location.getLongitude();
                                Log.i("debug999", "Lat: " + DiagnosisActivity.this.lat.toString() + " Lon: " + DiagnosisActivity.this.lon.toString());
                                calculateDistances(DiagnosisActivity.this.lon, DiagnosisActivity.this.lat);
                                recorrerMap();
                            }
                        }
                    });
                }
            }
    }

    //Esta se va. Es para probar
    public void recorrerMap() {
        for (TreeMap.Entry<Double, Hospital> entry : distancesArray.entrySet()) {
            Double key = entry.getKey();
            Hospital value = entry.getValue();
           // Log.i("debug38", "Posicion " + key.toString() + ": " + value.toString());
        }
        Log.i("debug38", "Hospital mas cercano: " + distancesArray.firstEntry().toString());
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

    private void diagnosis(){

    }

}