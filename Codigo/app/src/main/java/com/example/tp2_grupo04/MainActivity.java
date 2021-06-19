package com.example.tp2_grupo04;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnSend;
    private TextView tvBatteryLevel;
    private EditText telephoneNumber;
    private ImageView imageViewBattery;
    private AlertDialog alertDialog;
    private float batteryPct;
    private boolean isCharging;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = (Button) findViewById(R.id.btnSendCode);
        telephoneNumber = (EditText) findViewById(R.id.editTextTelephoneNumber);
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = MainActivity.this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        batteryPct = level * 100 / (float) scale;
        tvBatteryLevel = (TextView) findViewById(R.id.textViewBatteryLevel);
        tvBatteryLevel.setText(String.valueOf(batteryPct));
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        imageViewBattery = (ImageView) findViewById(R.id.imageViewBattery);
        if (isCharging) {
            imageViewBattery.setImageResource(R.drawable.ic_battery_charging_90_black_48dp);
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


    public void onClickSendCode(View view) {
        Intent intent;
        if (telephoneNumber.getText().toString().length() != 10) {
            setAlertText("Error!", "Ingrese un número de telefono con el formato 11xxxxxxxx.");
        } else if (telephoneNumber.getText().toString().matches("")) {
            setAlertText("Error!", "Debe ingresar un número de telefono.");
        } else {
            intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.putExtra("numeroTelefono", telephoneNumber.getText().toString());
            startActivity(intent);
            finish();
        }
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