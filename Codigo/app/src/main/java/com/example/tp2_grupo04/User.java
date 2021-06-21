package com.example.tp2_grupo04;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TimerTask;


public class User {

    private String email;
    private String password;
    private String token;
    private String token_refresh;

    public User(String email, String password, String token, String token_refresh) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.token_refresh = token_refresh;
    }

    public User() {
        this.email = "";
        this.password = "";
        this.token = "";
        this.token_refresh = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_refresh() {
        return token_refresh;
    }

    public void setToken_refresh(String token_refresh) {
        this.token_refresh = token_refresh;
    }

    public void refreshTokenTask() {
        setRepeatingAsyncTask();
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", token_refresh='" + token_refresh + '\'' +
                '}';
    }

    /*
        Asynctask donde se realiza la comunicacion con el servidor para el refresco del token.
        Se envia token y token de refresh del usuario.
     */
    class TokenTask extends AsyncTask<String, Void, Boolean> {

        private URL url;
        private HttpURLConnection connection = null;


        @Override
        protected Boolean doInBackground(String... strings) {
            JSONObject answer;
            String result;
            if (!Utils.isInternetAvailable()) {
                return false;
            }
            try {
                url = new URL(Utils.URI_TOKEN_REFRESH);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", "Bearer " + token_refresh);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("PUT");
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(strings[0].getBytes("UTF-8"));
                dataOutputStream.flush();
                connection.connect();
                Integer responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    result = Utils.convertInputStreamToString(inputStreamReader).toString();
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                    result = Utils.convertInputStreamToString(inputStreamReader).toString();
                } else {
                    result = "NOT_OK";
                }
                dataOutputStream.close();
                connection.disconnect();
                answer = new JSONObject(result);
                result = answer.get("success").toString();
                User.this.setToken(answer.get("token").toString());
                User.this.setToken_refresh(answer.get("token_refresh").toString());
                if (!result.matches("true")) {
                    return false;
                }
                return true;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean o) {
            if (!o) {
                Log.e("Error Token", "No se pudo refrescar el token");
            }
            Log.i("Token Server Response", "Me respondió: " + o.toString());
        }
    }

    /*
        Se setea el timer cada 25 minutos para realice la comunicacion con el servidor para refrescar el token del usuario.
     */
    private void setRepeatingAsyncTask() {
        final Handler handler = new Handler();
        TimerTaskClass ttc = new TimerTaskClass();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            TokenTask tokenTask = new TokenTask();
                            tokenTask.execute(getToken_refresh());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        ttc.initTimer();
        ttc.getInstance().getTimer().schedule(task, 0, 25 * 60 * 1000);
    }
}
