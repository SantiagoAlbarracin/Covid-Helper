package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    public Button btnRegister;
    public Button btnLogin;
    public EditText loginEmail;
    public EditText loginPassword;
    public ProgressBar progressBar;
    public TextView etiqWrongPass;
    public TextView etiqWrongEmail;
    public TextView etiqEmpty;

    private String result;


    public Boolean loginResponse = false;

    public URL url;
    public HttpURLConnection connection = null;
    public DataOutputStream dataOutputStream;

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
        if(checkFields()) {
            new LoginAsyncTask(LoginActivity.this).execute(loginEmail.getText().toString(), loginPassword.getText().toString());
        }
    }

    public void lanzarActivity(String... strings) {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("email", strings[0]);
        intent.putExtra("password", strings[1]);
        intent.putExtra("token", strings[2]);
        intent.putExtra("token_refresh", strings[3]);
        startActivity(intent);
        finish();
    }

    public boolean checkFields(){
        if(loginEmail.getText().toString().matches("") || loginPassword.getText().toString().matches("") ) {
            //etiqEmpty.setVisibility(View.VISIBLE);
            return false;
        }else if (!Utils.validate(loginEmail.getText().toString())) {
            //etiqWrongEmail.setVisibility(View.VISIBLE);
            return false;

        } else if (loginPassword.getText().toString().length() < 8){
            //etiqWrongPass.setVisibility(View.VISIBLE);
            return false;
        }
        return true;

    }

}