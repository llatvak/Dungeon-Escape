package fi.tamk.gameproject;

import fi.tamk.rentogames.PedometerStatus;

public class AndroidPedometerStatus implements PedometerStatus {

    AndroidLauncher launcher;

    boolean running;

    @Override
    public void setStatus(int status) {

        if(status == 1) {
            System.out.println("Start");
            running = true;
            //startPedometer();
        }
        if(status == 2) {
            System.out.println("Start");
            running = false;
            //stopPedometer();
        }
    }

    @Override
    public boolean getStatus() {
        return running;
    }
}
