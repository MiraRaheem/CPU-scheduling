import java.util.ArrayList;
import java.util.Collections;


public class PriorityScheduler
{
    ArrayList<Process> PS = new ArrayList<>();
    ArrayList<Process> PSID = new ArrayList<>();
    public static int time = 0 ;
    double averageTurnaround ;
    double averageWaitingTime ;

    PriorityScheduler (ArrayList<Process> arrayList)
    {

        for(int i=0 ; i < arrayList.size() ; i++)
            PS.add(arrayList.get(i));

        Collections.sort(PS , new PriorityComparator());
        PSAlg();
    }

    public void PSAlg ()
    {
        boolean checkLag;

        while (PS.size() !=0)
        {
            checkLag = false;

            for ( int i=0 ; i< PS.size() ; i++)
            {
                if (PS.get(i).arrivalTime <= time )
                {
                    checkLag = true ;
                    Process p = PS.get(i);

                    p.start = time;
                    p.end = time + p.burstTime ;
                    time += p.burstTime ;

                    p.turnAround = p.end-p.arrivalTime ;
                    p.waitingTime = p.turnAround - p.burstTime ;

                    PSID.add(p);
                    PS.remove(i);
                    Collections.sort(PSID , new IDComparator());

                    for(int j = 0 ; j < PS.size() ; j++)
                    {
                        if(PS.get(j).priorityNum > 5)
                            PS.get(j).priorityNum-- ;
                    }

                    Collections.sort(PS , new PriorityComparator());
                    break;
                }
            }
            if ( checkLag == false )
                time++;
        }
        new GanttChart("Priority Scheduler", PSID);
    }

    public void Average()
    {
        ArrayList<Process> temp = new ArrayList<>(PSID);

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
