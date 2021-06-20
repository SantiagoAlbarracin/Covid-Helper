package com.example.tp2_grupo04;

import android.os.AsyncTask;
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
    private Boolean loginSucces = false;
    private Boolean internetConnection = false;
    private String serverResponse;

    public LoginAsyncTask(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    /*
        Se realiza la comunicacion con el servidor. Se envia mail y contraseña.
        Se recibirá un mensaje por parte del servidor informando si el logueo fue exitoso o no.
     */
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
            dataOutputStream.close();
            connection.disconnect();
            answer = new JSONObject(result);
            result = answer.get("success").toString();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        if (!result.matches("true")) {
            try {
                serverResponse = answer.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            user = new User(strings[0], strings[1], answer.get("token").toString(), answer.get("token_refresh").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    /*
        Se inhabilitan los botones y se muestra el progress bar hasta que se reciba respuesta del servidor.
     */
    @Override
    protected void onPreExecute() {
        this.loginActivity.progressBar.setVisibility(View.VISIBLE);
        this.loginActivity.btnLogin.setEnabled(false);
        this.loginActivity.btnRegister.setEnabled(false);
    }

    /*
        En caso de que el logueo haya sido exitoso, se envia al usuario a la activity Menu y se ejecuta el metodo que
        realiza el refresco del token cada 25 minutos mediante la comunicacion con el servidor.
        Si el usuario no tiene conexion a internet, se le informara que debe conectarse a internet e intentar nuevamente.
        En caso de que la respuesta del servidor no haya sido de exito, se le informara el problema al usuario.
     */
    @Override
    protected void onPostExecute(Boolean o) {
        if (o) {
            this.loginActivity.lanzarActivity(user.getEmail(), user.getPassword(),
                    user.getToken(), user.getToken_refresh());
            executeRefresh(user.getEmail(), user.getPassword(),
                    user.getToken(), user.getToken_refresh());
        } else {
            if (!loginSucces && internetConnection) {
                this.loginActivity.setAlertText("Error de Logueo!", serverResponse);
            }
            if (!internetConnection) {
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

