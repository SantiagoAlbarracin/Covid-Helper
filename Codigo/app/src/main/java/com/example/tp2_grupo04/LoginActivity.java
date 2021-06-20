package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    /*
        Se crea la activity Login, se genera un SP para el manejo de informacion de contador de pasos,
        que al lanzar la activity Menu se inicializará.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AppInfo", "<<<<ON_CREATE LOGIN_ACTIVITY>>>>");
        setContentView(R.layout.activity_login);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        loginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        sp = this.getSharedPreferences(Utils.SP_STEP_TIME, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppInfo", "<<<<ON_STOP LOGIN_ACTIVITY>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AppInfo", "<<<<ON_DESTROY LOGIN_ACTIVITY>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AppInfo", "<<<<ON_PAUSE LOGIN_ACTIVITY>>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("AppInfo", "<<<<ON_START LOGIN_ACTIVITY>>>>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppInfo", "<<<<ON_RESUME LOGIN_ACTIVITY>>>>");
    }

    /*
        Se dirige al usuario a la activity Register cuando este presiona el boton Registrarse.
     */
    public void onClickRegister(View view) {
        Intent intent;
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    /*
        Cuando el usuario presiona el boton Iniciar Sesion, se verifica que los campos de mail
        y contraseña no esten vacios y cumplan formato. Posteriormente se inicia un Asynctask que se comunica con el servidor.
        Se le bloquearán los botones al usuario y mostrará un progress bar hasta que se reciba respuesta por parte del servidor.
        En caso afirmativo se dirige al usuario a la activity Menu, en caso negativo se le informará cual fue el error para
        que pueda loguearse nuevamente.
     */
    public void onClickLogin(View view) {
        if (checkFields()) {
            new LoginAsyncTask(LoginActivity.this).execute(loginEmail.getText().toString(), loginPassword.getText().toString());
        }
    }

    /*
       Se lanza la activity Menu, se inicia un Asynctask que cada 25 minutos refrescara el token autenticacion asignado al usuario
       comunicandose con el servidor.
       Adicionalmente se almacena en el SharedPreferences generado en el onCreate los valores que seran utilizados por el contador de pasos.
    */
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
        editor.putString(StepCounterActivity.ACTIVE_TIME, "");
        editor.commit();
        startActivity(intent);
        finish();
    }

    /*
        Se chequea que los campos de mail y contraseña cumplan con los formatos:
        - Formato de mail xxx@xxx.xxx
        - Contraseña mayor a 8 caracteres

        Si cumplen con los formatos se retornará TRUE, en caso contrario, FALSE.
     */
    public boolean checkFields() {
        if (loginEmail.getText().toString().matches("") || loginPassword.getText().toString().matches("")) {
            setAlertText("Error de Logueo!", "Debe completar todos los campos.");
            return false;
        } else if (!Utils.validate(loginEmail.getText().toString())) {
            setAlertText("Error de Logueo!", "Debe ingresar un mail valido.");
            return false;

        } else if (loginPassword.getText().toString().length() < 8) {
            setAlertText("Error de Logueo!", "Debe ingresar una contraseña valida.");
            return false;
        }
        return true;

    }

    /*
        Se realiza el seteo de titulo y mensaje para las alertas al usuario.
     */
    public void setAlertText(String title, String message) {
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