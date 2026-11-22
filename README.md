# CPU Scheduling Simulator

A JavaFX-based desktop application that simulates and compares CPU scheduling algorithms with step-by-step execution traces and performance metrics.

## Features

- **Scheduling Algorithms**:
  - **Preemptive Shortest-Job First (SJF)**: Dynamically preempts the running process whenever a newly arrived process has a strictly shorter remaining burst time[cite: 11].
  - **Adaptive Container Scheduler (ACS)**: A multi-tier feedback queue architecture combining **Tier 1** (Round Robin with configurable time quantum) and **Tier 2** (Non-preemptive priority scheduling with dynamic priority aging to prevent starvation)[cite: 9].
- **Configurable Simulation Parameters**: Supports custom context switch overhead times and Tier 1 Round Robin quantum sizes.
- **Interactive JavaFX GUI**:
  - Add and clear custom processes (`ID`, `Arrival Time`, `Burst Time`, `Priority`).
  - View computed per-process metrics (`Finish`, `Turnaround`, `Waiting`, and `Response` times) alongside overall averages[cite: 10].
  - Inspect real-time, per-second execution logs showing CPU allocation, idle ticks, context switches, and Tier 1 / Tier 2 queue states[cite: 9, 11].

## Project Structure

```text
src/main/
 ├── java/org/savana/cpuschedulersimulator/
 │    ├── App.java                  # JavaFX application entry point
 │    ├── SchedulerController.java  # UI controller and simulation runner
 │    ├── Process.java              # Process model and metric calculations
 │    ├── SJFScheduler.java         # Preemptive SJF scheduling engine
 │    └── ACSScheduler.java         # Adaptive Container Scheduler engine
 └── resources/org/savana/cpuschedulersimulator/
      ├── scheduler.fxml            # FXML layout definition
      └── style.css                 # Application styling