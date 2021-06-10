package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private EditText loginEmail;
    private EditText loginPassword;
    private ProgressBar progressBar;
    private TextView etiqWrongPass;
    private TextView etiqWrongEmail;
    private TextView etiqEmpty;

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


        btnRegister.setOnClickListener(buttonsListeners);
        btnLogin.setOnClickListener(buttonsListeners);
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


    private View.OnClickListener buttonsListeners = new View.OnClickListener()
    {
        public void onClick (View v){
            Intent intent;
            switch (v.getId())
            {
                case R.id.btnRegister:
                    intent=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.btnLogin:

                    new LoginTask().execute(loginEmail.getText().toString(), loginPassword.getText().toString());
                    break;

                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_SHORT);
            }
        }
    };


    class LoginTask extends AsyncTask<String, Void, Boolean>{

        private Intent intent;

        @Override
        protected Boolean doInBackground(String... strings) {

            if(loginEmail.getText().toString().matches("") || loginPassword.getText().toString().matches("") ) {

                //etiqEmpty.setVisibility(View.VISIBLE);

            }else if (!Utils.validate(loginEmail.getText().toString())) {
                //etiqWrongEmail.setVisibility(View.VISIBLE);
            } else if (loginPassword.getText().toString().length() < 8){
                //etiqWrongPass.setVisibility(View.VISIBLE);
            }else {
                JSONObject object = new JSONObject();
                try {
                    object.put("env", "TEST");
                    object.put("email", strings[0]);
                    object.put("password", strings[1]);

                    Intent i = new Intent(LoginActivity.this, ServiceHTTP_POST.class);
                    i.putExtra("uri", Utils.URI_LOGIN_USER);
                    i.putExtra("datosJson", object.toString());

                    startService(i);

                    Thread.sleep(500);
                } catch (InterruptedException | JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }

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
            if(o) {
                intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();

            }
        }

    }


}