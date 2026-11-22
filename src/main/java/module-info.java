module org.savana.cpuschedulersimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.savana.cpuschedulersimulator to javafx.fxml;
    exports org.savana.cpuschedulersimulator;
}