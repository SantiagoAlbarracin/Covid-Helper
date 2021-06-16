package com.example.tp2_grupo04;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EventAsyncTask extends AsyncTask<String, Void, Boolean> {


    private DiagnosisActivity diagnosisActivity;
    private User user;
    private Boolean sendEvent=false;
    private Boolean internetConnection=false;

    public EventAsyncTask(DiagnosisActivity diagnosisActivity) {
        this.diagnosisActivity = diagnosisActivity;
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
            String token_refresh = strings[2];

            URL url = new URL(Utils.URI_REGISTER_EVENT);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + token_refresh);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(object.toString().getBytes("UTF-8"));

            Log.i("debug104", "Se envia al servidor " + object.toString());

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
            Log.i("debug83", "Me contestó " + result);

            dataOutputStream.close();
            connection.disconnect();

            answer = new JSONObject(result);

            result = answer.get("success").toString();


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        if (result.matches("true")) {
            Log.i("debug166", "Llegó: " + answer.toString());
            return true;
        }

        return false;
    }

    @Override
    protected void onPreExecute() {
        /*
        this.diagnosisActivity.progressBar.setVisibility(View.VISIBLE);
        this.diagnosisActivity.btnLogin.setEnabled(false);
        this.diagnosisActivity.btnRegister.setEnabled(false);
        */
    }


    @Override
    protected void onPostExecute(Boolean o) {
        /*if (o) {
            this.loginActivity.lanzarActivity(user.getEmail(), user.getPassword(),
                    user.getToken(), user.getToken_refresh());
        } else {
            if (!loginSucces && internetConnection) {
                this.diagnosisActivity.setAlertText("Error al Enviar!", "Intente nuevamente.");
            }
            if (!internetConnection){
                this.diagnosisActivity.setAlertText("Error de conexion!", "Debe conectarse a internet e intentar nuevamente");
            }
            this.diagnosisActivity.progressBar.setVisibility(View.INVISIBLE);
            this.diagnosisActivity.btnLogin.setEnabled(true);
            this.diagnosisActivity.btnRegister.setEnabled(true);
        }

         */
    }



}
