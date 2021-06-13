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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
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

    public User (){
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

    public void refreshTokenTask(){
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

    class TokenTask extends AsyncTask<String, Void, Boolean> {

        private URL url;
        private HttpURLConnection connection = null;

        @Override
        protected Boolean doInBackground(String... strings) {
            JSONObject answer;
            String result;

            try {

                url = new URL(Utils.URI_TOKEN_REFRESH);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", "Bearer " + token_refresh);
                Log.i("debug666", "STRINGS 0 TIENE " + strings[0]);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("PUT");

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(strings[0].getBytes("UTF-8"));

                Log.i("debug666", "Se envia al servidor " + strings[0]);

                dataOutputStream.flush();

                connection.connect();

                Integer responseCode = connection.getResponseCode();
                Log.i("debug666", " RECIBI EL RESPONSECODE " + responseCode.toString());

                if( responseCode == HttpURLConnection.HTTP_OK ){
                    Log.i("debug666", " ENTRE AL IF PORQUE FUE OK");

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    result = Utils.convertInputStreamToString(inputStreamReader).toString();
                }
                else if( responseCode == HttpURLConnection.HTTP_BAD_REQUEST ){
                    Log.i("debug666", " ENTRE AL ELSE PORQUE FUE BAD REQUEST");

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                    result = Utils.convertInputStreamToString(inputStreamReader).toString();
                }
                else{
                    result = "NOT_OK";
                    Log.i("debug666", " RECIBI EL RESULT, FUE NO OK");
                }

                dataOutputStream.close();

                connection.disconnect();

                answer = new JSONObject(result);
                result = answer.get("success").toString();
                Log.i("debug666", "Se recibio " + result);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    };


    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            TokenTask tokenTask = new TokenTask();
                            tokenTask.execute(getToken_refresh().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        //timer.schedule(task, 0, 30*60*1000);
        timer.schedule(task, 0, 30*1000);

    }
}
