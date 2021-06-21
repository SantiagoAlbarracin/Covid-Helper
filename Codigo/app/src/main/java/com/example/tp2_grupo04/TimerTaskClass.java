package com.example.tp2_grupo04;

import java.util.Timer;

public class TimerTaskClass {

    /*
        Variables estaticas que son accesibles en las activities.
     */
    private static TimerTaskClass instance;
    private static Timer timer;

    public TimerTaskClass() {
    }

    /*
        Metodo sincronizado donde se inicializa el timer en caso de que ninguna activity lo haya hecho.
     */
    public synchronized void initTimer() {
        if (this.timer == null) {
            this.timer = new Timer();
        }
    }

    /*
        Metodo sincronizado donde se inicializa la instancia de la clase en caso de que ninguna activity lo haya hecho.
        En caso de que ya haya sido creada, se retorna la instancia.
     */
    public synchronized TimerTaskClass getInstance() {
        if (instance == null) {
            instance = new TimerTaskClass();
        }
        return instance;
    }

    public Timer getTimer() {
        return this.timer;
    }

    /*
        Se cancela el timer que ejecuta el metodo del refresco del token cada 25 minutos.
     */
    public synchronized void stopTimer() {
        this.timer.cancel();
    }

}