import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Process
{
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priorityNum;
    int waitingTime;
    int turnAround;
    int start;
    int end;
    int pID;
    int quantum;
    int timeLeft;
    int AGfactor;

    List<Integer> S_E = new ArrayList<>();

    public static int ID = 1;

    Process(String n, String c, int at, int bt, int pt, int q)
    {
        pID = ID ;
        ID++;
        name = n ;
        color = c ;
        arrivalTime = at;
        burstTime = bt;
        priorityNum = pt;
        quantum = q;
        start = 0 ;
        end = 0 ;
        waitingTime = 0;
        turnAround = 0;
        timeLeft = burstTime;
        AGfactor = arrivalTime + burstTime + priorityNum;
    }

    public String toString()
    {
        return "PID: " + pID + " Name: " + name + ", colour: " + color + ", arrival time: " + arrivalTime +
                ", burst time: " + burstTime + ", pt: " + priorityNum + ", q: " + quantum +", time left: " + timeLeft ;
    }
}

class IDComparator implements Comparator<Process>
{
    @Override
    public int compare(Process o1, Process o2) { return o1.pID - o2.pID;}
}

class BurstComparator implements Comparator<Process>
{
    @Override
    public int compare(Process o1, Process o2) { return o1.burstTime - o2.burstTime;}
}

class PriorityComparator implements Comparator<Process>
{
    @Override
    public int compare(Process o1, Process o2) { return o1.priorityNum - o2.priorityNum;}
}

class ProcessComparator implements Comparator<Process>
{
    @Override
    public int compare(Process o1, Process o2) {
        return o1.timeLeft - o2.timeLeft;
    }
}