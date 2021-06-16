package com.example.tp2_grupo04;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tp2_grupo04.LoginActivity;
import com.example.tp2_grupo04.MenuActivity;

public class DistanceSensorAsyncTask extends AsyncTask<Void, Void, Boolean>  {


    private long initialTime;
    private MenuActivity menuActivity;

    public DistanceSensorAsyncTask(MenuActivity menuActivity) {
        this.menuActivity = menuActivity;
    }

    /*@Override
    protected Boolean onPreExecute(Void... voids) {

            return true;
    }*/


    @Override
    protected Boolean doInBackground(Void... voids) {
        this.initialTime=System.currentTimeMillis();
        long actualTime;
        while((actualTime=(System.currentTimeMillis() - initialTime)) <= 1000 && menuActivity.isCloseDistance()){

        }
        if(actualTime > 1000){ //salio del while porque pasó 1 segundo
            Log.i("Debug34", String.valueOf(actualTime));
            //entonces el usuario no hizo el gesto
            return false;
        }else{ //salio del while porque el sensor no detecta nada cerca
            //entonces el usuario hizo el gesto
            Log.i("Debug39", String.valueOf(actualTime));
            return true;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            this.menuActivity.lanzarActivityDiagnosis();
        }
    }


}