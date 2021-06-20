package com.example.tp2_grupo04;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class AuthenticationActivity extends AppCompatActivity {

    private Button btnAccept;
    private Button btnCancel;
    private Button btnSendCode;
    private EditText userCode;
    private Integer random = -1;
    private String number = "";
    private AlertDialog alertDialog;

    /*
        Creacion de la activity Authentication. Se genera el codigo de manera random para autentificar.
        Este codigo es enviado al numero telefonico que el usuario ingresó en la activity Main.
        Si no se tiene permiso para enviar mensajes, este es solicitado.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AppInfo", "<<<<ON_CREATE AUTHENTICATION_ACTIVITY>>>>");
        setContentView(R.layout.activity_authentication);
        btnAccept = (Button) findViewById(R.id.btnAcceptAuth);
        btnCancel = (Button) findViewById(R.id.btnCancelAuth);
        btnSendCode = (Button) findViewById(R.id.btnSendCodeAuth);
        userCode = (EditText) findViewById(R.id.editTextCode);
        alertDialog = new AlertDialog.Builder(AuthenticationActivity.this).create();
        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();
        number = (String) extras.get("numeroTelefono");
        random = (int) (Math.random() * 10000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendSMS(number);
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }
    }

    /*
        Metodo que envia el sms al usuario con el codigo de verificación.
     */
    private void sendSMS(String number2) {
        String number = number2;
        random = (int) (Math.random() * 10000);
        String message = "Su codigo de verificacion es:" + random.toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(this, "El mensaje ha sido enviado", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "El mensaje no se pudo enviar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppInfo", "<<<<ON_STOP AUTHENTICATION_ACTIVITY>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AppInfo", "<<<<ON_DESTROY AUTHENTICATION_ACTIVITY>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AppInfo", "<<<<ON_PAUSE AUTHENTICATION_ACTIVITY>>>>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppInfo", "<<<<ON_RESUME AUTHENTICATION_ACTIVITY>>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("AppInfo", "<<<<ON_START AUTHENTICATION_ACTIVITY>>>>");
    }

    /*
        Cuando el usuario presiona el boton Aceptar, se verifica que el codigo ingresado coincida con el enviado.
        En caso de que sea correcto, se lo dirige a la activity Login.
     */
    public void onClickAccept(View view) {
        Intent intent;
        if (userCode.getText().toString().matches("")) {
            setAlertText("Error!", "Debe ingresar un codigo.");
        } else {
            if (random.toString().equals(userCode.getText().toString())) {
                intent = new Intent(AuthenticationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                setAlertText("Error!", "Codigo Incorrecto. Intente nuevamente.");
            }
        }
    }

    /*
       Cuando el usuario presiona el boton Cancelar, se lo dirige a la activity Authentication.
    */
    public void onClickCancel(View view) {
        Intent intent;
        intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*
       Cuando el usuario presiona el boton Volver a Enviar, se le envia un nuevo mensaje con un codigo de autentificacion nuevo.
     */
    public void onClickSendCode(View view) {
        sendSMS(number);
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

    /*
       Cuando el usuario presiona el boton Cancelar, se lo dirige a la activity Authentication.
    */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}