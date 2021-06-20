package com.example.tp2_grupo04;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventAsyncTask extends AsyncTask<String, Void, Boolean> {

    private LoginActivity loginActivity;
    private User user;
    private Boolean sendEvent = false;
    private Boolean internetConnection = false;

    public EventAsyncTask(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        JSONObject answer = null;
        JSONObject object = new JSONObject();
        String result = null;
        if (!Utils.isInternetAvailable()) {
            internetConnection = false;
            return false;
        }
        try {
            internetConnection = true;
            object.put("env", "TEST");
            object.put("type_events", strings[0]);
            object.put("description", strings[1]);
            String token = strings[2];
            URL url = new URL(Utils.URI_REGISTER_EVENT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(object.toString().getBytes("UTF-8"));
            dataOutputStream.flush();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = Utils.convertInputStreamToString(inputStreamReader).toString();
                sendEvent = true;
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = Utils.convertInputStreamToString(inputStreamReader).toString();
                sendEvent = false;
            } else {
                result = "NOT_OK";
                sendEvent = false;
            }
            dataOutputStream.close();
            connection.disconnect();
            answer = new JSONObject(result);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        if (!result.matches("true")) {
            try {
                result = answer.get("success").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean o) {
        if (o) {
        } else {
            if (!sendEvent && internetConnection) {
                this.loginActivity.setAlertText("Error al Enviar!", "Intente nuevamente.");
            }
            if (!internetConnection) {
                this.loginActivity.setAlertText("Error de conexion!", "Debe conectarse a internet e intentar nuevamente");
            }
        }
    }


}
