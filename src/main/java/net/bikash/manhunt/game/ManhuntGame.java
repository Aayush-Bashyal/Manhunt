package net.bikash.manhunt.game;

public class ManhuntGame {
    private boolean running = false;
    public boolean isRunning(){
        return running;
    }
    public void start(){
        running = true;
    }
    public void stop(){
        running=false;
    }

}
