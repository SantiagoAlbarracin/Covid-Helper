package com.example.tp2_grupo04;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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
    private Integer random = 1;
    private String number = "";
    private AlertDialog alertDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        btnAccept = (Button) findViewById(R.id.btnAcceptAuth);
        btnCancel = (Button) findViewById(R.id.btnCancelAuth);
        btnSendCode = (Button) findViewById(R.id.btnSendCodeAuth);
        userCode = (EditText) findViewById(R.id.editTextCode);
        alertDialog = new AlertDialog.Builder(AuthenticationActivity.this).create();

        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();
        number = (String) extras.get("numeroTelefono");

        //random = (int) (Math.random() * 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                //   sendSMS(number);
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }


    }

    private void sendSMS(String number2) {
        String number = number2;
        //Log.i("debug70", number);
        //random = (int) (Math.random() * 1000);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


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
                Toast.makeText(AuthenticationActivity.this,
                        "Codigo Incorrecto. Intente nuevamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickCancel(View view) {
        Intent intent;
        intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSendCode(View view) {
        sendSMS(number);
    }

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