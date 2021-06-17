package com.example.tp2_grupo04;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnSend;
    private TextView tvBatteryLevel;
    private EditText telephoneNumber;
    private ImageView imageViewBattery;
    private float batteryPct;
    private boolean isCharging;

    //BatteryManager myBatteryManager = (BatteryManager) MainActivity.this.getSystemService(Context.BATTERY_SERVICE);

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend=(Button)findViewById(R.id.btnSendCode);
        telephoneNumber=(EditText)findViewById(R.id.editTextTelephoneNumber);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = MainActivity.this.registerReceiver(null, ifilter);
        //status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        batteryPct = level * 100 / (float)scale;
        tvBatteryLevel = (TextView)findViewById(R.id.textViewBatteryLevel);
        tvBatteryLevel.setText(String.valueOf(batteryPct));
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        imageViewBattery=(ImageView)findViewById(R.id.imageViewBattery);
        if(isCharging){
            imageViewBattery.setImageResource(R.drawable.ic_battery_charging_90_black_48dp);
        }

        Log.i("Bateria", String.valueOf(batteryPct));
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