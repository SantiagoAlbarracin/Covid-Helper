package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private EditText loginEmail;
    private EditText loginPassword;
    private ProgressBar progressBar;
    private TextView etiqWrongPass;
    private TextView etiqWrongEmail;
    private TextView etiqEmpty;

    public Boolean loginResponse = false;

    public URL url;
    public HttpURLConnection connection = null;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        loginEmail=(EditText)findViewById(R.id.editTextLoginEmail);
        loginPassword=(EditText)findViewById(R.id.editTextLoginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        etiqWrongPass = (TextView) findViewById(R.id.etiqWrongPassword);
        etiqWrongEmail = (TextView) findViewById(R.id.etiqWrongEmail);
        etiqEmpty = (TextView) findViewById(R.id.etiqEmpty);

        etiqWrongPass.setVisibility(View.GONE);
        etiqWrongEmail.setVisibility(View.GONE);
        etiqEmpty.setVisibility(View.GONE);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void onClickRegister(View view){
        Intent intent;
        intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickLogin(View view){
        Intent intent;
        try {
            new LoginTask().execute(loginEmail.getText().toString(), loginPassword.getText().toString()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(loginResponse) {
            Log.i("debug102", " El sv Respondio SUCCESS TRUE");
            intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            intent.putExtra("email", user.getEmail().toString());
            intent.putExtra("password", user.getPassword().toString());
            intent.putExtra("token", user.getToken().toString());
            intent.putExtra("token_refresh", user.getToken_refresh().toString());
            startActivity(intent);
            finish();
        }else {
            Log.i("debug102", " Sv respondio SUCCESS FALSE");
        }
    }



    class LoginTask extends AsyncTask<String, Void, Boolean>{

        private Intent intent;
        private String result;

        @Override
        protected Boolean doInBackground(String... strings) {
            JSONObject answer = null;
            if(loginEmail.getText().toString().matches("") || loginPassword.getText().toString().matches("") ) {
                //etiqEmpty.setVisibility(View.VISIBLE);
            }else if (!Utils.validate(loginEmail.getText().toString())) {
                //etiqWrongEmail.setVisibility(View.VISIBLE);
            } else if (loginPassword.getText().toString().length() < 8){
                //etiqWrongPass.setVisibility(View.VISIBLE);
            }else {
                JSONObject object = new JSONObject();
                try {
                    object.put("email", strings[0]);
                    object.put("password", strings[1]);

                    url = new URL(Utils.URI_LOGIN_USER);

                    connection = (HttpURLConnection) url.openConnection();
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

                    if( responseCode == HttpURLConnection.HTTP_OK ){
                        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                        result = Utils.convertInputStreamToString(inputStreamReader).toString();

                    }
                    else if( responseCode == HttpURLConnection.HTTP_BAD_REQUEST ){

                        InputStreamReader inputStreamReader = new InputStreamReader(connection.getErrorStream());
                        result = Utils.convertInputStreamToString(inputStreamReader).toString();

                    }
                    else{
                        result = "NOT_OK";
                    }

                    dataOutputStream.close();
                    connection.disconnect();

                    answer = new JSONObject(result);

                    result = answer.get("success").toString();


                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

                if(result.matches("true")){
                    loginResponse = true;
                    try {
                        user = new User(strings[0], strings[1], answer.get("token").toString(), answer.get("token_refresh").toString());
                        Log.i("debug166", user.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("debug238", "Login Response tiene " + loginResponse.toString());
                    return true;
                }
                else{
                    loginResponse = false;
                    Log.i("debug238", "Login Response tiene " + loginResponse.toString());
                    return false;
                }
            }
            loginResponse = false;
            Log.i("debug238", "Login Response tiene " + loginResponse.toString());
            return false;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            btnRegister.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Boolean o) {
            progressBar.setVisibility(View.INVISIBLE);
            btnLogin.setEnabled(true);
            btnRegister.setEnabled(true);
        }

    }

}