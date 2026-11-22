import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ACSScheduler {
    private List<Process> process;
    private Queue<Process> tier1;
    private List<Process> tier2;
    private int contextSwitch;
    private int quantum;

    public ACSScheduler(List<Process> process, int contextSwitch, int quantum) {
        this.contextSwitch = contextSwitch;
        this.process = process;
        this.quantum = quantum;
        this.tier1 = new LinkedList<>();
        this.tier2 = new ArrayList<>();
    }

    public void increasePriority() {
        for (Process each : tier2) {
            each.priority++;
        }
    }

    public void refresh(int second) {
        for (Process each : process) {
            if (each.arrivalTime <= second && !each.isAdded) {
                tier1.add(each);
                each.isAdded = true;
            }
        }
    }

    public void run() {
        int second = 0;
        int completed = 0;
        Process old = null, current;
        while (completed < process.size()) {
            refresh(second);
            if (!tier1.isEmpty()) {
                current = tier1.poll();
                if (old != current) {
                    if (old != null) {
                        for (int i = 0; i < contextSwitch; i++) {
                            second++;
                            refresh(second);
                            increasePriority();
                        }
                        System.out.println(
                                "Time " + second + ": Context switch saves" + old.id + " and loads" + current.id);
                    }
                    old = current;
                }
                if (current.firstTime == -1) {
                    current.firstTime = second;
                }
                for (int i = 1; i < quantum + 1 && current.remainingTime > 0; i++) {
                    System.out.println("Time " + second + ": Running P" + current.id + " (Tier1) | Tier 1: " + tier1
                            + "| Tier 2: " + tier2);
                    current.remainingTime--;
                    second++;
                    refresh(second);
                    increasePriority();
                }
                if (current.remainingTime == 0) {
                    completed++;
                    current.finishTime = second;
                } else {
                    tier2.add(current);
                }
                current = null;
            } else if (!tier2.isEmpty()) {
                current = tier2.get(0);
                for (Process each : tier2) {
                    if (each.priority > current.priority) {
                        current = each;
                    }
                }
                tier2.remove(current);
                if (old != current) {
                    if (old != null) {
                        for (int i = 0; i < contextSwitch; i++) {
                            second++;
                            refresh(second);
                            increasePriority();
                        }
                        System.out.println(
                                "Time " + second + ": Context switch saves " + old.id + " and loads " + current.id);
                    }
                    old = current;
                }
                while (current.remainingTime > 0) {
                    System.out.println("Time " + second + ": Running P" + current.id + " (Tier2) | Tier 1: " + tier1
                            + "| Tier 2: " + tier2);

                    second++;
                    refresh(second);
                    increasePriority();
                    current.remainingTime--;
                }
                completed++;
                current.finishTime = second;
                current = null;
            } else {
                System.out.println("Time " + second + ": Idle");
                second++;
            }
        }
        for (Process each : process) {
            each.calculate();
        }
    }
}
