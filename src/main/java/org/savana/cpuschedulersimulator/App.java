package org.savana.cpuschedulersimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scheduler.fxml"));
        Scene scene = new Scene(loader.load(), 820, 680);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("CPU Scheduling Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}