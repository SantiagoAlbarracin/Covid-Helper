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


    public void onClickSendCode(View view){
        Intent intent;
        Log.i("debug59", "entre a onclicksendcode");
        if(telephoneNumber.getText().toString().length() < 10){
            Toast.makeText(MainActivity.this,
                    "Ingrese un número de telefono valido.", Toast.LENGTH_SHORT).show();
        } else if(telephoneNumber.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this,
                    "Ingrese un número de telefono.", Toast.LENGTH_SHORT).show();
        }else{
            intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.putExtra("numeroTelefono", telephoneNumber.getText().toString());
            startActivity(intent);
            finish();
        }
    }


}