import java.util.*;

public class SRTF
{
    //public static PriorityQueue<Process> srtfTL = new PriorityQueue<>((o1, o2) -> (o1.timeLeft > o2.timeLeft ) ? 1 : -1);
    public static ArrayList<Process> srtfTL = new ArrayList<>() ;
    public static ArrayList<Process> srtfID = new ArrayList<>();
    int contextSwitching;
    public static int time = 0 ;

    SRTF (ArrayList<Process> arrayList, int cs)
    {
        for(int i=0 ; i < arrayList.size() ; i++)
            srtfTL.add(arrayList.get(i));

        contextSwitching = cs;

        SRTFAlg();
    }

    public void SRTFAlg ()
    {
        int preID = -1 , tempStart , tempEnd;
        boolean checkLag;

        while (srtfTL.size() != 0)
        {
            checkLag = false;
            for(int i = 0 ; i < srtfTL.size() ;i++)
            {
                if (srtfTL.get(i).arrivalTime <= time)
                {
                    checkLag = true;
                    Process p = srtfTL.get(i);

                    if(preID != -1 && preID != p.pID)
                        time+= contextSwitching;

                    if(p.timeLeft > p.quantum)
                    {
                        tempStart = time ;
                        tempEnd = tempStart + p.quantum ;

                        p.S_E.add(tempStart);
                        p.S_E.add(tempEnd) ;

                        time += p.quantum ;
                        p.timeLeft -= p.quantum ;

                        srtfTL.remove(i);
                        srtfTL.add(p) ;

                        Collections.sort(srtfTL , new ProcessComparator());

                    }
                    else
                    {

                        tempStart = time ;
                        tempEnd = tempStart + p.timeLeft ;

                        p.S_E.add(tempStart);
                        p.S_E.add(tempEnd) ;

                        p.start = p.S_E.get(0);
                        p.end = p.S_E.get(p.S_E.size()-1);

                        time += p.timeLeft ;
                        p.turnAround = (p.end - p.arrivalTime) + contextSwitching ;
                        p.waitingTime = p.turnAround - p.burstTime ;
                        p.timeLeft = 0 ;

                        srtfID.add(p);
                        Collections.sort(srtfID , new IDComparator());
                        srtfTL.remove(i);

                        Collections.sort(srtfTL , new ProcessComparator());
                    }
                    preID = p.pID ;
                    break ;
                }
            }
            if ( checkLag == false )
                time++;
        }
        time+= contextSwitching ;
        new GanttChart("SRTF", srtfID);
    }

    public void Average()
    {
        ArrayList<Process> temp = new ArrayList<>(srtfID);
        double averageTurnaround , averageWaitingTime;

        int tempTA = 0 , tempWT = 0;

        for(int i = 0 ; i < temp.size() ;i++)
        {
            tempTA += temp.get(i).turnAround;
            tempWT += temp.get(i).waitingTime;
        }
        System.out.println("temp TA" + tempTA);
        System.out.println("temp Tw" + tempWT);

        averageTurnaround = (tempTA*1.0) / (temp.size()*1.0) ;
        averageWaitingTime = (tempWT*1.0) / (temp.size()*1.0) ;

        System.out.println(" average Turnaround = " + averageTurnaround);
        System.out.println(" average Waiting Time = " + averageWaitingTime);
    }
}


