package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

        alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();

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

    public void onClickAccept(View view){
            if(verifyRegisterFields()){
            new RegisterAsyncTask(RegisterActivity.this).execute(nameOrigin.getText().toString(), lastnameOrigin.getText().toString(),
                    dniOrigin.getText().toString(), emailOrigin.getText().toString(),
                    passwordOrigin.getText().toString(), commissionOrigin.getText().toString(),
                    groupOrigin.getText().toString());
        }
    }

    public void onClickCancel(View view){
        Intent intent;
        intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void configureBroadcastReceiver(){
        filter = new IntentFilter("com.example.httoconnection_intentservice.intent.action.RESPUESTA_OPERACION");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    public void lanzarActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean verifyRegisterFields(){
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
        } else if (passwordOrigin.getText().toString().length() < 8){
            setAlertText("Error de Registro!", "Debe ingresar una contraseña de 8 caracteres o más.");
            return false;
        } else if(dniOrigin.getText().toString().length() < 7){
            setAlertText("Error de Registro!", "Debe ingresar un DNI valido.");
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