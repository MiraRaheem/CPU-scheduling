import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class SJF
{
    ArrayList<Process> sjfBurst = new ArrayList<>();
    ArrayList<Process> sjfID = new ArrayList<>();
    public static int time = 0 ;

    SJF (ArrayList<Process> arrayList)
    {
        System.out.println(arrayList.size());
        for(int i=0 ; i < arrayList.size() ; i++)
        {
            sjfBurst.add(arrayList.get(i));
        }

        Collections.sort(sjfBurst, new BurstComparator());
        SJFAlg();
    }

    public void SJFAlg ()
    {
        boolean checkLag ;

        while (sjfBurst.size() !=0)
        {
            checkLag = false;
            for ( int i=0 ; i< sjfBurst.size() ; i++)
            {
                if (sjfBurst.get(i).arrivalTime <= time )
                {
                    checkLag = true;
                    Process t = sjfBurst.get(i);
                    t.start = time;
                    t.S_E.add(t.start);

                    t.end = time + t.burstTime ;
                    time += t.burstTime ;
                    t.S_E.add(t.end);

                    t.waitingTime = t.start - t.arrivalTime ;
                    t.turnAround = t.end-t.arrivalTime ;
                    sjfID.add(t);

                    Collections.sort(sjfID, new IDComparator());
                    sjfBurst.remove(i);
                    break;
                }
            }
            if(checkLag == false)
                time++;
        }
        new GanttChart("SJF", sjfID);
    }

    public void Average()
    {
        ArrayList<Process> temp = new ArrayList<>(sjfID);
        double averageTurnaround , averageWaitingTime;

        int tempTA = 0 , tempWT = 0;

        for(int i = 0 ; i < temp.size() ;i++)
        {
            tempTA += temp.get(i).turnAround;
            tempWT += temp.get(i).waitingTime;
        }

        averageTurnaround = (tempTA*1.0) / (temp.size()*1.0) ;
        averageWaitingTime = (tempWT*1.0) / (temp.size()*1.0) ;

        System.out.println(" average Turnaround = " + averageTurnaround);
        System.out.println(" average Waiting Time = " + averageWaitingTime);
    }
}
