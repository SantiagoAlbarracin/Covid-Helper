package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


        btnAccept.setOnClickListener(buttonsListeners);
        btnCancel.setOnClickListener(buttonsListeners);
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
                case R.id.btnAccept:
                    intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    User user = new User ();
                    user.setName(nameOrigin.getText().toString());
                    user.setLastname(lastnameOrigin.getText().toString());
                    user.setDni(Integer.valueOf(dniOrigin.getText().toString()));
                    user.setEmail(emailOrigin.getText().toString());
                    user.setPassword(passwordOrigin.getText().toString());
                    user.setCommission(Integer.valueOf(commissionOrigin.getText().toString()));
                    user.setGroup(Integer.valueOf(groupOrigin.getText().toString()));
                    Log.i("debug 84",user.toString());
                    System.out.println(user.toString());
                    finish();
                    break;

                case R.id.btnCancel:
                    intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_SHORT);
            }
        }
    };
}