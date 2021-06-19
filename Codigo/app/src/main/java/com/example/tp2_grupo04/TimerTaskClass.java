package com.example.tp2_grupo04;

import java.util.Timer;

public class TimerTaskClass {

    private static TimerTaskClass instance;
    private static Timer timer;

    public TimerTaskClass() {}

    public void initTimer(){
        this.timer = new Timer();
    }

    public synchronized TimerTaskClass getInstance() {
        if (instance == null)
        {
            instance = new TimerTaskClass ();
        }
        return instance;
    }

    public Timer getTimer(){
        return this.timer;
    }

    public void stopTimer(){
        this.timer.cancel();
    }

}