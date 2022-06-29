import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static int menu() {
        int num;

        System.out.println("1- SJF");
        System.out.println("2- SRTF");
        System.out.println("3- Priority Scheduler ");
        System.out.println("4- AG Scheduler ");

        Scanner input = new Scanner(System.in);
        num = input.nextInt();

        return num;

    }

    public static void main(String[] args) {
        ArrayList<Process> processArrayList = new ArrayList<>();
        String name;
        String color;
        int arrivalTime;
        int burstTime;
        int priorityNum;

        Scanner input = new Scanner(System.in);

        int numOfProcess;
        int rrQuantum;
        int contextProcess;
        int choiceNum;

        System.out.println("Enter number of process");
        numOfProcess = input.nextInt();

        System.out.println("Enter quantum number ");
        rrQuantum = input.nextInt();


        System.out.println("Enter Context Switching ");
        contextProcess = input.nextInt();

        for (int i = 0; i < numOfProcess; i++) {
            System.out.println("Enter process name");
            name = input.next();
            System.out.println("Enter process color");
            color = input.next();
            System.out.println("Enter process arrival time");
            arrivalTime = input.nextInt();
            System.out.println("Enter process burst time");
            burstTime = input.nextInt();
            System.out.println("Enter process priority");
            priorityNum = input.nextInt();

            Process p = new Process(name, color, arrivalTime, burstTime, priorityNum, rrQuantum);
            processArrayList.add(p);
        }

            choiceNum = menu();

            if (choiceNum == 1) {
                SJF s = new SJF(processArrayList);
                s.Average();
            } else if (choiceNum == 2) {
                SRTF s = new SRTF(processArrayList, contextProcess);
                s.Average();
            } else if (choiceNum == 3) {
                PriorityScheduler p = new PriorityScheduler(processArrayList);
                p.Average();
            } else if (choiceNum == 4) {
                AG a = new AG(processArrayList);
                a.Average();
            }
    }
}
