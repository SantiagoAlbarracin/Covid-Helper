package com.example.tp2_grupo04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText telephoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend=(Button)findViewById(R.id.btnSendCode);
        telephoneNumber=(EditText)findViewById(R.id.editTextTelephoneNumber);
        btnSend.setOnClickListener(buttonsListeners);
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
                case R.id.btnSendCode:
                   if(!telephoneNumber.getText().toString().matches("")) {
                       intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                       startActivity(intent);
                       finish();
                   }
                   else{
                       Toast.makeText(MainActivity.this,
                               "Ingrese un número de telefono.", Toast.LENGTH_SHORT).show();
                   }
                    break;

                default:
                    Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_SHORT);
            }
        }
    };
}