import java.util.ArrayList;
import java.util.List;

public class SJFScheduler {
    private int contextSwitch;
    private List<Process> process;
    private List<Process> ready;

    public SJFScheduler(List<Process> process, int contextSwitch) {
        this.contextSwitch = contextSwitch;
        this.process = process;
        ready = new ArrayList<>();
    }

    public void run() {
        int second = 0;
        int completed = 0;
        Process old = null;
        while (completed < process.size()) {
            for (Process each : process) {
                if (each.arrivalTime <= second && !each.isAdded) {
                    ready.add(each);
                    each.isAdded = true;
                }
            }
            if (!ready.isEmpty()) {
                Process shortest = ready.get(0);
                for (Process p : ready) {
                    if (p.remainingTime < shortest.remainingTime) {
                        shortest = p;
                    }
                }
                if (shortest != old) {
                    if (old != null) {
                        System.out.println(
                                "Time " + second + ": Context switch saves " + old.id + " and loads " + shortest.id);
                        second += contextSwitch;
                    }
                    old = shortest;
                }
                if (shortest.firstTime == -1) {
                    shortest.firstTime = second;
                }
                System.out.println(
                        "Time " + second + ": Running " + shortest.id + " (remaining: " + shortest.remainingTime + ")");
                second++;
                shortest.remainingTime--;
                if (shortest.remainingTime <= 0) {
                    completed++;
                    shortest.finishTime = second;
                    ready.remove(shortest);
                }
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
