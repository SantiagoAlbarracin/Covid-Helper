package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DiagnosisActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    private HashMap<Integer, Hospital> hospitalsArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        generateHospitals();
        recorrerMap();



    }

    //Esta se va. Es para probar
    public void recorrerMap(){
        for (HashMap.Entry<Integer, Hospital> entry : hospitalsArray.entrySet()) {
            Integer key = entry.getKey();
            Hospital value = entry.getValue();
            Log.i("debug38", "Posicion " + key.toString() + ": " + value.toString());
        }
    }


    public void generateHospitals(){

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
                String coordinates =  geometryArray.getString("coordinates");
                hospital = new Hospital(name, address, telephone, coordinates);
                hospitalsArray.put(i, hospital);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public String readFile(){
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


}