package com.example.tp2_grupo04;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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


public class DiagnosisActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    private HashMap<Integer, Hospital> hospitalsArray;
    private Button btnLocation;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Double lat;
    private Double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        btnLocation = (Button) findViewById(R.id.btnLocation);

       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        generateHospitals();
        recorrerMap();
        getLocation();

    }


    private void getLocation(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
                if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.i("debug999", "Lat: " + lat.toString() + " Lon: " + lon.toString());
                            }
                        }
                    });
                }
            }
    }

    //Esta se va. Es para probar
    public void recorrerMap() {
        for (HashMap.Entry<Integer, Hospital> entry : hospitalsArray.entrySet()) {
            Integer key = entry.getKey();
            Hospital value = entry.getValue();
            Log.i("debug38", "Posicion " + key.toString() + ": " + value.toString());
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


    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {

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


}