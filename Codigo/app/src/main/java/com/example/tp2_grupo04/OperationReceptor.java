package com.example.tp2_grupo04;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class OperationReceptor extends BroadcastReceiver {

    public OperationReceptor() {
    }

    public void onReceive(Context context, Intent intent){
        try{
            String dataJsonString = intent.getStringExtra("datosJson");
            JSONObject jsonObject = new JSONObject(dataJsonString);

            Toast.makeText(context.getApplicationContext(), "Respondió el server.", Toast.LENGTH_SHORT);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
