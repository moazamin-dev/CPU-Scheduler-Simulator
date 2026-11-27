package org.savana.cpuschedulersimulator;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("TEST CASE 1 (P1: 0,7,3 | P2: 3,4,2 | P3: 6,5,5)");
        System.out.println("==========================================");

        // Test Case 1 - Fixed q = 3, varying cs = 1, 2, 3
        int[] tc1CsValues = {1, 2, 3};
        for (int cs : tc1CsValues) {
            runCase("Test Case 1", createTestCase1(), cs, 3);
        }

        // Test Case 1 - Fixed cs = 1, varying q = 2, 3, 4
        int[] tc1QValues = {2, 3, 4};
        for (int q : tc1QValues) {
            runCase("Test Case 1", createTestCase1(), 1, q);
        }

        System.out.println("\n==========================================");
        System.out.println("TEST CASE 2 (P1: 0,6,4 | P2: 4,9,1 | P3: 8,2,9)");
        System.out.println("==========================================");

        // Test Case 2 - Fixed cs = 2, varying q = 4, 5, 6
        int[] tc2QValues = {4, 5, 6};
        for (int q : tc2QValues) {
            runCase("Test Case 2", createTestCase2(), 2, q);
        }

        // Test Case 2 - Fixed q = 2, varying cs = 1, 2, 3
        int[] tc2CsValues = {1, 2, 3};
        for (int cs : tc2CsValues) {
            runCase("Test Case 2", createTestCase2(), cs, 2);
        }
    }

    private static List<Process> createTestCase1() {
        List<Process> list = new ArrayList<>();
        list.add(new Process(1, 0, 7, 3));
        list.add(new Process(2, 3, 4, 2));
        list.add(new Process(3, 6, 5, 5));
        return list;
    }

    private static List<Process> createTestCase2() {
        List<Process> list = new ArrayList<>();
        list.add(new Process(1, 0, 6, 4));
        list.add(new Process(2, 4, 9, 1));
        list.add(new Process(3, 8, 2, 9));
        return list;
    }

    private static void runCase(String label, List<Process> processes, int cs, int q) {
        System.out.println("\n------------------------------------------");
        System.out.printf("%s | Cs = %d | q = %d%n", label, cs, q);
        System.out.println("------------------------------------------");

        ACSScheduler scheduler = new ACSScheduler(processes, cs, q);
        scheduler.run();

        double totalTurnaround = 0;
        double totalWaiting = 0;
        double totalResponse = 0;

        System.out.println("\nProcess Metrics:");
        System.out.printf("%-8s %-8s %-8s %-12s %-10s %-10s%n",
                "Process", "Finish", "Turn.", "Waiting", "Response", "FinalPrio");
        for (Process p : processes) {
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
            totalResponse += p.responseTime;
            System.out.printf("P%-7d %-8d %-8d %-12d %-10d %-10d%n",
                    p.id, p.finishTime, p.turnaroundTime, p.waitingTime, p.responseTime, p.priority);
        }

        int n = processes.size();
        System.out.printf("Average Turnaround Time (ATT): %.2f%n", totalTurnaround / n);
        System.out.printf("Average Waiting Time (AWT):    %.2f%n", totalWaiting / n);
        System.out.printf("Average Response Time (ART):   %.2f%n", totalResponse / n);
    }
}