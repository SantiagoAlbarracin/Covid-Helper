package com.example.tp2_grupo04;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {

    private LoginActivity loginActivity;
    private User user;
    private Boolean loginSucces=false;
    private Boolean internetConnection=false;

    public LoginAsyncTask(LoginActivity loginActivity) {
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
            object.put("email", strings[0]);
            object.put("password", strings[1]);

            URL url = new URL(Utils.URI_LOGIN_USER);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                result = Utils.convertInputStreamToString(inputStreamReader).toString();
                loginSucces = true;

            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                result = Utils.convertInputStreamToString(inputStreamReader).toString();
                loginSucces = false;

            } else {
                result = "NOT_OK";
                loginSucces = false;

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
            try {
                user = new User(strings[0], strings[1], answer.get("token").toString(), answer.get("token_refresh").toString());
                Log.i("debug166", user.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    @Override
    protected void onPreExecute() {
        this.loginActivity.progressBar.setVisibility(View.VISIBLE);
        this.loginActivity.btnLogin.setEnabled(false);
        this.loginActivity.btnRegister.setEnabled(false);
    }

    @Override
    protected void onPostExecute(Boolean o) {
        if (o) {
            this.loginActivity.lanzarActivity(user.getEmail(), user.getPassword(),
                    user.getToken(), user.getToken_refresh());

            executeRefresh(user.getEmail(), user.getPassword(),
                    user.getToken(), user.getToken_refresh());
        } else {
            if (!loginSucces && internetConnection) {
                this.loginActivity.setAlertText("Error de Logueo!", "Mail y Contraseña no validos.");
            }
            if (!internetConnection){
                this.loginActivity.setAlertText("Error de conexion!", "Debe conectarse a internet e intentar nuevamente");
            }
            this.loginActivity.progressBar.setVisibility(View.INVISIBLE);
            this.loginActivity.btnLogin.setEnabled(true);
            this.loginActivity.btnRegister.setEnabled(true);
        }
    }

    private void executeRefresh(String... strings) {
        User user = new User(strings[0], strings[1], strings[2], strings[3]);
        user.refreshTokenTask();
    }
}

