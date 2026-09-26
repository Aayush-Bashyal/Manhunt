package net.bikash.manhunt.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManhuntGame {
    private boolean running = false;
    private UUID runner;
    private final List<UUID> hunters = new ArrayList<>();
    private long startTime;
    private long finalTime;
    private String winner;

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

public void finish(String winner){
        finalTime = getElapsedTime();
        this.winner = winner;
        stop();
}
public long getFinalTime(){
        return finalTime;
}
public String getWinner(){
        return winner;
}

public String getFinalTimeFormatted(){
    long totalSeconds = finalTime / 1000;

    long minutes = totalSeconds / 60;
    long seconds = totalSeconds % 60;
    
    return String.format(
            "%02d:%02d",
            minutes,
            seconds
    );
}
public boolean removeHunter(UUID hunter) {
      return hunters.remove(hunter);
}
public void removeRunner(){
        runner = null;
}
}
