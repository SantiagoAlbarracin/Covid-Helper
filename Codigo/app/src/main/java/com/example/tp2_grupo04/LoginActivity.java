package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnLogin;
    private EditText loginEmail;
    //public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        loginEmail=(EditText)findViewById(R.id.editTextLoginEmail);
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


    /** Called when the user taps the Send button */
    /*public void sendMessage(View view) {
        if()
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }*/
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

                  //  EN EL INTENT EN VEZ DE IR A AUTHENTICATION HAY QUE IR AL MENU PRINCIPAL
                case R.id.btnLogin:
                    intent=new Intent(LoginActivity.this,MainMenuActivity.class);
                    intent.putExtra("loginEmail",loginEmail.getText().toString());
                    startActivity(intent);
                    finish();
                    break;


                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_SHORT);
            }
        }
    };
}