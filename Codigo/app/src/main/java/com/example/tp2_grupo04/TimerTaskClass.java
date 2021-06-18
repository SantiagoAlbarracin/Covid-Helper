package com.example.tp2_grupo04;

import java.util.Timer;

public class TimerTaskClass {

    private static TimerTaskClass _instance;
    private static Timer timer;

    public TimerTaskClass() {
    }


    public void initTimer(){
        this.timer = new Timer();
    }

    public synchronized TimerTaskClass getInstance()
    {
        if (_instance == null)
        {
            _instance = new TimerTaskClass ();
        }
        return _instance;
    }

    public Timer getTimer(){
        return this.timer;
    }

    public void stopTimer(){
        this.timer.cancel();
    }

}

