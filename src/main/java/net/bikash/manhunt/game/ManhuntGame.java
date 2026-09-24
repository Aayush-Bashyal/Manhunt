package net.bikash.manhunt.game;

import java.util.UUID;

public class ManhuntGame {
    private boolean running = false;
    private UUID runner;
    public boolean isRunning(){
        return running;
    }
    public void start(){
        running = true;
    }
    public void stop(){
        running=false;
    }
    public void setRunner(UUID runner){
        this.runner = runner;

    }
    public UUID getRunner(){
        return runner;
    }


}
