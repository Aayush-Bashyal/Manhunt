package net.bikash.manhunt.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManhuntGame {
    private boolean running = false;
    private UUID runner;
    private final List<UUID> hunters = new ArrayList<>();
    private long startTime;

    public boolean isRunning(){
        return running;
    }
    public void start(){

        running = true;
        startTime  = System.currentTimeMillis();
    }

    public long getElapsedTime(){
        if(!running){
            return  0;
        }
    return System.currentTimeMillis()-startTime;
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
public void addHunter(UUID hunter){
        if(!hunters.contains(hunter)){
            hunters.add(hunter);
        }
}
public List<UUID> getHunters(){
        return hunters;
}


}
