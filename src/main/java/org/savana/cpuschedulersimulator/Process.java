package org.savana.cpuschedulersimulator;

public class Process {
    public int id;
    public int arrivalTime;
    public int firstTime;
    public int burstTime;
    public int remainingTime;
    public int priority;
    public int finishTime;
    public int waitingTime;
    public int turnaroundTime;
    public int responseTime;
    public boolean isAdded;
    public Process(int id, int arrivalTime, int burstTime, int priority){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.firstTime = -1;
        this.isAdded = false;
    }
    public void calculate(){
        turnaroundTime = finishTime - arrivalTime;
        waitingTime = turnaroundTime - burstTime;
        responseTime = firstTime - arrivalTime;
    }
    @Override 
    public String toString(){
        return "P" + id + " (rem = " + remainingTime + " , prio = " + priority + ")";
    }
}
