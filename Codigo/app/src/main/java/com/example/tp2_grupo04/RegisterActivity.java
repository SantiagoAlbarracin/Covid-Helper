package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {

    public Button btnAccept;
    public Button btnCancel;
    private EditText nameOrigin;
    private EditText lastnameOrigin;
    private EditText dniOrigin;
    private EditText emailOrigin;
    private EditText passwordOrigin;
    private EditText commissionOrigin;
    private EditText groupOrigin;
    public ProgressBar progressBar;
    private AlertDialog alertDialog;

    /*
        Se crea la activity Register y se inicializan todos los botones, campos de texto, progress bar y alertas.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AppInfo", "<<<<ON_CREATE REGISTERA_ACTIVITY>>>>");
        setContentView(R.layout.activity_register);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        nameOrigin = (EditText) findViewById(R.id.editTextName);
        lastnameOrigin = (EditText) findViewById(R.id.editTextLastname);
        dniOrigin = (EditText) findViewById(R.id.editTextDni);
        emailOrigin = (EditText) findViewById(R.id.editTextEmail);
        passwordOrigin = (EditText) findViewById(R.id.editTextPassword);
        commissionOrigin = (EditText) findViewById(R.id.editTextCommission);
        groupOrigin = (EditText) findViewById(R.id.editTextGroup);
        progressBar = (ProgressBar) findViewById(R.id.progressBarReg);
        alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AppInfo", "<<<<ON_STOP REGISTERA_ACTIVITY>>>>");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AppInfo", "<<<<ON_DESTROY REGISTERA_ACTIVITY>>>>");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AppInfo", "<<<<ON_PAUSE REGISTERA_ACTIVITY>>>>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AppInfo", "<<<<ON_RESUME REGISTERA_ACTIVITY>>>>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("AppInfo", "<<<<ON_START REGISTERA_ACTIVITY>>>>");
    }

    /*
        Cuando el usuario presiona el boton back de la Android UI, se lo dirige a la activity Login.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*
        Cuando el usuario presiona el boton Registrarse, se verifica que se hayan completado todos los campos y que cumplan con los formatos solicitados.
        En caso de que cumplan, se inicia un Asynctask que se comunica con el servidor para realizar el registro del usuario.
        En caso contrario se le informara al usuario del error.
        Si la respuesta del servidor es exito, se enviará al usuario a la activity Login.
        Si la respuesta del servidor NO es de exito, se le informará al usuario del problema y se lo dejará en la activity Register
        para que intente nuevamente.
        Al inciar el asynctask se bloquean los botones y se muestra un progress bar hasta que se reciba respuesta por parte del servidor.
     */
    public void onClickAccept(View view) {
        if (verifyRegisterFields()) {
            new RegisterAsyncTask(RegisterActivity.this).execute(nameOrigin.getText().toString(), lastnameOrigin.getText().toString(),
                    dniOrigin.getText().toString(), emailOrigin.getText().toString(),
                    passwordOrigin.getText().toString(), commissionOrigin.getText().toString(),
                    groupOrigin.getText().toString());
        }
    }

    /*
        Si el usuario presiona el boton Cancelar, se lo dirige a la activity Login.
     */
    public void onClickCancel(View view) {
        Intent intent;
        intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*
        Metodo utilizado para lanzar la activity Login desde el Asynctask en caso de que la respuesta del servidor sea de exito.
     */
    public void lanzarActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*
        Se verifican los campos de texto:
        -   Que no esten vacios
        -   Que la contraseña de 8 caracteres o mas
        -   Que el mail cumpla con el formato
        -   Que el DNI sea valido. 7 u 8 nros.

        En caso de que cumplan se retorna TRUE, sino FALSE.
     */
    private boolean verifyRegisterFields() {
        if (nameOrigin.getText().toString().matches("") || lastnameOrigin.getText().toString().matches("")
                || dniOrigin.getText().toString().matches("") || emailOrigin.getText().toString().matches("")
                || passwordOrigin.getText().toString().matches("") || commissionOrigin.getText().toString().matches("")
                || groupOrigin.getText().toString().matches("")
        ) {
            setAlertText("Error de Registro!", "Debe completar todos los campos.");
            return false;
        } else if (!Utils.validate(emailOrigin.getText().toString())) {
            setAlertText("Error de Registro!", "Debe ingresar un mail valido.");
            return false;
        } else if (passwordOrigin.getText().toString().length() < 8) {
            setAlertText("Error de Registro!", "Debe ingresar una contraseña de 8 caracteres o más.");
            return false;
        } else if (dniOrigin.getText().toString().length() < 7 || dniOrigin.getText().toString().length() > 8) {
            setAlertText("Error de Registro!", "Debe ingresar un DNI valido.");
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