package com.example.tp2_grupo04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServiceHTTP_POST extends AppCompatActivity {

    URL url;
    HttpURLConnection connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("debug24","Se inicio el servicio");

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onHandleIntent(Intent intent){

        try {
            String uri = intent.getExtras().getString("uri");
            JSONObject jsonObject = new JSONObject(intent.getExtras().getString("dataJSON"));

            executePost(uri, jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("debug66","Error " + e.toString());
        }
    }

    private StringBuilder convertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( (line = br.readLine()) != null ){
            stringBuilder.append(line + "\n");
        }
        br.close();
        return stringBuilder;
    }

    private String POST(String uri, JSONObject jsonObject){

        String result = "";
        try{
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(jsonObject.toString().getBytes("UTF-8"));

            Log.i("debug104", "Se envia al servidor " + dataOutputStream.toString());

            dataOutputStream.flush();
            connection.connect();

            int responseCode = connection.getResponseCode();

            if( responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED ){
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStreamReader).toString();
            }else if( responseCode == HttpURLConnection.HTTP_BAD_REQUEST ){
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStreamReader).toString();
            }
            else{
                result = "NOT_OK";
            }

            dataOutputStream.close();
            connection.disconnect();

            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void executePost(String uri, JSONObject jsonObject){
        String result = POST(uri,jsonObject);

        if(result == null){
            Log.i("debug140", "Error en el GET de POST");
            return;
        }

        if(result == "NOT_OK"){
            Log.e("debug145", "Se recibio response NOT_OK");
            return;
        }

        Intent intent = new Intent("com.example.httoconnection_intentservice.intent.action.RESPUESTA_OPERACION");
        intent.putExtra("dataJSON", result);
        sendBroadcast(intent);

    }

}
