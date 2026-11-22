package org.savana.cpuschedulersimulator;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SchedulerController {

    @FXML private TextField idField, arrivalField, burstField, priorityField;
    @FXML private TextField csField, quantumField;
    @FXML private ComboBox<String> algoBox;
    @FXML private TableView<Process> processTable;
    @FXML private TableColumn<Process, Number> idCol, arrivalCol, burstCol, priorityCol;
    @FXML private TableColumn<Process, Number> finishCol, turnaroundCol, waitingCol, responseCol;
    @FXML private Label avgLabel;
    @FXML private TextArea logArea;

    private final ObservableList<Process> processList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        algoBox.getItems().addAll("Preemptive SJF", "Adaptive Container Scheduler (ACS)");
        algoBox.setValue("Preemptive SJF");

        idCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().id));
        arrivalCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().arrivalTime));
        burstCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().burstTime));
        priorityCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().priority));
        finishCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().finishTime));
        turnaroundCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().turnaroundTime));
        waitingCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().waitingTime));
        responseCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().responseTime));

        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        processTable.setItems(processList);

    }

    @FXML
    private void handleAddProcess() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int arrival = Integer.parseInt(arrivalField.getText().trim());
            int burst = Integer.parseInt(burstField.getText().trim());
            int priority = Integer.parseInt(priorityField.getText().trim());

            processList.add(new Process(id, arrival, burst, priority));
            idField.clear();
            arrivalField.clear();
            burstField.clear();
            priorityField.clear();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Enter valid integers for all process fields.").showAndWait();
        }
    }

    @FXML
    private void handleClear() {
        processList.clear();
        logArea.clear();
        avgLabel.setText("Averages: -");
    }

    @FXML
    private void handleRun() {
        if (processList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Add at least one process first.").showAndWait();
            return;
        }

        int cs, quantum;
        try {
            cs = Integer.parseInt(csField.getText().trim());
            quantum = Integer.parseInt(quantumField.getText().trim());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Context Switch and Quantum must be integers.").showAndWait();
            return;
        }

        List<Process> freshList = new ArrayList<>();
        for (Process p : processList) {
            freshList.add(new Process(p.id, p.arrivalTime, p.burstTime, p.priority));
        }

        logArea.clear();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                logArea.appendText(String.valueOf((char) b));
            }
        }));

        try {
            if (algoBox.getValue().equals("Preemptive SJF")) {
                new SJFScheduler(freshList, cs).run();
            } else {
                new ACSScheduler(freshList, cs, quantum).run();
            }
        } finally {
            System.setOut(oldOut);
        }

        processList.setAll(freshList);
        processTable.refresh();

        double avgTurn = freshList.stream().mapToInt(p -> p.turnaroundTime).average().orElse(0);
        double avgWait = freshList.stream().mapToInt(p -> p.waitingTime).average().orElse(0);
        double avgResp = freshList.stream().mapToInt(p -> p.responseTime).average().orElse(0);

        avgLabel.setText(String.format(
                "Average Turnaround: %.2f   |   Average Waiting: %.2f   |   Average Response: %.2f",
                avgTurn, avgWait, avgResp
        ));
    }
}