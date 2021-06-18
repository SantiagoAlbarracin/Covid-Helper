package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {

    public Button btnRegister;
    public Button btnLogin;
    public EditText loginEmail;
    public EditText loginPassword;
    public ProgressBar progressBar;
    private SharedPreferences sp;

    private String userEmail;
    private String userToken;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        loginEmail=(EditText)findViewById(R.id.editTextLoginEmail);
        loginPassword=(EditText)findViewById(R.id.editTextLoginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        sp = this.getSharedPreferences(Utils.SP_STEP_TIME, Context.MODE_PRIVATE);


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
        userEmail = strings[0];
        userToken = strings[2];
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String eventDescription = "User Login " + userEmail + " at " + formatter.format(date).toString();
        new EventAsyncTask(LoginActivity.this).execute(Utils.TYPE_EVENT, eventDescription, userToken);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(StepCounterActivity.ACTUAL_STEPS, "");
       // editor.putString(StepCounterActivity.INITIAL_TIME, "");
        editor.putString(StepCounterActivity.ACTIVE_TIME, "");
        editor.commit();
        startActivity(intent);
        finish();
    }

    public boolean checkFields(){
        if(loginEmail.getText().toString().matches("") || loginPassword.getText().toString().matches("") ) {
            setAlertText("Error de Logueo!", "Debe completar todos los campos.");
            return false;
        }else if (!Utils.validate(loginEmail.getText().toString())) {
            setAlertText("Error de Logueo!", "Debe ingresar un mail valido.");
            return false;

        } else if (loginPassword.getText().toString().length() < 8){
            setAlertText("Error de Logueo!", "Debe ingresar una contraseña valida.");
            return false;
        }
        return true;

    }


    public void setAlertText(String title, String message){
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}