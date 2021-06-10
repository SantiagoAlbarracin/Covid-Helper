package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {

    private Button btnAccept;
    private Button btnCancel;
    private EditText nameOrigin;
    private EditText lastnameOrigin;
    private EditText dniOrigin;
    private EditText emailOrigin;
    private EditText passwordOrigin;
    private EditText commissionOrigin;
    private EditText groupOrigin;
    private ProgressBar progressBar;

    public IntentFilter filter;
    private OperationReceptor receiver = new OperationReceptor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnAccept=(Button)findViewById(R.id.btnAccept);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        nameOrigin=(EditText)findViewById(R.id.editTextName);
        lastnameOrigin=(EditText)findViewById(R.id.editTextLastname);
        dniOrigin=(EditText)findViewById(R.id.editTextDni);
        emailOrigin=(EditText)findViewById(R.id.editTextEmail);
        passwordOrigin=(EditText)findViewById(R.id.editTextPassword);
        commissionOrigin=(EditText)findViewById(R.id.editTextCommission);
        groupOrigin=(EditText)findViewById(R.id.editTextGroup);
        progressBar = (ProgressBar) findViewById(R.id.progressBarReg);


        btnAccept.setOnClickListener(buttonsListeners);
        btnCancel.setOnClickListener(buttonsListeners);

        configureBroadcastReceiver();
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

    private View.OnClickListener buttonsListeners = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;

            switch (v.getId()) {
                case R.id.btnAccept:
                    new RegisterTask().execute(nameOrigin.getText().toString(), lastnameOrigin.getText().toString(),
                            dniOrigin.getText().toString(), emailOrigin.getText().toString(),
                            passwordOrigin.getText().toString(), commissionOrigin.getText().toString(),
                            groupOrigin.getText().toString());
                break;

                case R.id.btnCancel:
                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                break;

                default:
                    Toast.makeText(getApplicationContext(), "Error en Listener de botones", Toast.LENGTH_SHORT);
        }
    }

    };

    private void configureBroadcastReceiver(){
        filter = new IntentFilter("com.example.httoconnection_intentservice.intent.action.RESPUESTA_OPERACION");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }



    class RegisterTask extends AsyncTask<String, Void, Boolean> {

        private Intent intent;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnAccept.setEnabled(false);
            btnCancel.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            progressBar.setVisibility(View.INVISIBLE);
            btnAccept.setEnabled(true);
            btnCancel.setEnabled(true);

            if (aBoolean) {
                Log.i("debug102", " El sv Respondio OK Register");
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                Log.i("debug102", " Fallo conexion con sv Register");

            }

        }

        @Override
        protected Boolean doInBackground(String... objects) {

            if(VerifyRegisterFields()){

                JSONObject object = new JSONObject();
                try {
                    object.put("env", "TEST");
                    object.put("name", objects[0] );
                    object.put("lastname", objects[1]);
                    object.put("dni", Integer.valueOf(objects[2]));
                    object.put("email", objects[3]);
                    object.put("password", objects[4]);
                    object.put("commission", Integer.valueOf(objects[5]));
                    object.put("group", Integer.valueOf(objects[6]));

                    Intent i = new Intent(RegisterActivity.this, ServiceHTTP_POST.class);

                    i.putExtra("uri", Utils.URI_REGISTER_USER);
                    i.putExtra("dataJSON", object.toString());


                    startService(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                return true;
            }

            return false;
        }
    }



    private boolean VerifyRegisterFields(){
        if (nameOrigin.getText().toString().matches("") || lastnameOrigin.getText().toString().matches("")
                || dniOrigin.getText().toString().matches("") || emailOrigin.getText().toString().matches("")
                || passwordOrigin.getText().toString().matches("") || commissionOrigin.getText().toString().matches("")
                || groupOrigin.getText().toString().matches("")
        ) {

            return false;
        } else if (!Utils.validate(emailOrigin.getText().toString())) {

            return false;
        } else if (passwordOrigin.getText().toString().length() < 8){

            return false;
        } else if(dniOrigin.getText().toString().length() < 8){

            return false;
        }
        return true;
    }


}