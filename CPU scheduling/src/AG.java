import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AG {

    PriorityQueue<Process> AGPQ = new PriorityQueue<>((o1, o2) -> (o1.AGfactor > o2.AGfactor) ? 1 : -1);
    ArrayList<Process> AGID = new ArrayList<>();

    ArrayList<Process> tempAG ;
    ArrayList<Process> AGL=new ArrayList<>();
    int size ;

    public static int time = 0 ;

    AG(ArrayList<Process> arrayList)
    {
        for(int i=0 ; i < arrayList.size() ; i++)
            AGPQ.add(arrayList.get(i));

        tempAG=new ArrayList<>(AGPQ);
        size = AGPQ.size();
        AGAlg();

        // test contents of priority queue
        /*Iterator it = sjfBurst.iterator();
        while (it.hasNext())
        {
            System.out.println(it.next());
        }*/
    }

    public void Refill()
    {
        for (int i=0; i<tempAG.size();i++)
        {
            if(tempAG.get(i).arrivalTime<=time)
            {
                AGL.add(tempAG.get(i));
                tempAG.remove(i);
                i=0;
            }
        }

    }

    public Process BetterAG(Process T)
    {
        Process temp=T;
        for(int i=0; i<AGL.size();i++)
        {
            if(temp.AGfactor>AGL.get(i).AGfactor)
                temp = AGL.get(i);
        }

        if(temp.pID == T.pID)
            return null;
        else
            return temp;
    }

    public double Mean()
    {
        double mean = 0.0 ;
        for(int i=0; i<AGL.size();i++)
            mean += AGL.get(i).quantum ;

        return mean / AGL.size() * 1.0;
    }

    public void Remove(Process p)
    {
        for(int i = 0 ; i < AGL.size() ;i++)
        {
            if (p.pID == AGL.get(i).pID)
                AGL.remove(i);
        }
    }

    Process EndProcess (Process pro)
    {
        pro.start = pro.S_E.get(0) ;
        pro.end = pro.S_E.get(pro.S_E.size()-1) ;

        pro.turnAround = pro.end-pro.arrivalTime ;
        pro.waitingTime = pro.turnAround - pro.burstTime ;

        time += pro.timeLeft ;
        AGID.add(pro);
        Remove(pro);

        Collections.sort(AGID, new IDComparator());

        size--;

        while (AGL.size() == 0 && tempAG.size() !=0)
        {
            time++;
            Refill();
        }

        if (AGL.size() == 0 && tempAG.size() ==0)
            return null;

        return AGL.get(0);
    }

    public void AGAlg()
    {
        Refill();
        Process p = AGL.get(0);

        int tempStart , tempEnd ;

        while (size!=0)
        {
            if ( p.timeLeft <= Math.ceil( (p.quantum/2.0)))
            {
                tempStart = time ;

                tempEnd = time + p.timeLeft;

                p.S_E.add(tempStart);
                p.S_E.add(tempEnd);

                p = EndProcess(p);
            }
            else
            {
                tempStart = time ;
                time += Math.ceil((p.quantum/2.0));

                Refill();

                p.S_E.add(tempStart);

                if(BetterAG(p) != null)
                {
                    tempEnd = time ;
                    p.S_E.add(tempEnd);

                    p.timeLeft -= Math.ceil((p.quantum/2.0));

                    p.quantum += Math.floor((p.quantum/2.0)) ;

                    Remove(p);

                    AGL.add(p);

                    p = BetterAG(p) ;
                }
                else
                {
                    p.timeLeft -= Math.ceil((p.quantum/2.0)) ;
                    for (int i = 1 ; i <= Math.floor((p.quantum/2.0)) ; i++)
                    {
                        time++ ;
                        Refill();
                        p.timeLeft -= 1;

                        if(BetterAG(p) != null)
                        {
                            tempEnd = time ;
                            p.S_E.add(tempEnd);

                            if (p.timeLeft == 0)
                            {
                                p = EndProcess(p) ;
                                break;
                            }
                            else
                            {
                                p.quantum += Math.floor((p.quantum/2.0)) - i ; //Math.ceil((p.quantum/2)) + i

                                Remove(p);
                                AGL.add(p);

                                p = BetterAG(p) ;

                                break;
                            }

                        }

                        if( i == Math.floor((p.quantum/2.0)))
                        {
                            tempEnd = time ;
                            p.S_E.add(tempEnd);

                            if(p.timeLeft == 0)
                            {
                                p = EndProcess(p) ;
                                break;
                            }
                            else
                            {
                                p.quantum += Math.ceil(0.1 * Mean()) ;

                                Remove(p);
                                AGL.add(p);

                                p = AGL.get(0);
                                break;
                            }
                        }

                        if(p.timeLeft == 0) // 3ashan lw wa'te 5ls abl el mtb'e fl quantum
                        {
                            tempEnd = time ;
                            p.S_E.add(tempEnd) ;

                            p = EndProcess(p) ;
                            break;
                        }
                    }
                }

            }

        }
        new GanttChart("AG", AGID);
    }

    public void Average()
    {
        ArrayList<Process> temp = new ArrayList<>(AGID);

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
