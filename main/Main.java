package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    MainController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        primaryStage.setTitle("Epidemic Simulator");
        Path iconPath = Paths.get("", System.getProperty("user.dir"), "img", "icon.png");
        Image icon = new Image("file:" + iconPath.toString());
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // AnimationTimer updates UI by using data from controller in every second
        final long[] lastCallTime = {System.nanoTime()};
        new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                double elapsedSeconds = (currentTime - lastCallTime[0]) / 1000000000.0;
                if (elapsedSeconds > 1.0) {
                    lastCallTime[0] = currentTime;
                    System.out.println("AnimationTimer()::handle()::elapsedSeconds:" + elapsedSeconds);
                    controller.updateCanvas();
                    controller.updateCountStatistics();
                    controller.updateChart();
                }
            }
        }.start();
    }

    @Override
    public void stop() {
        controller.endSimulation();
    }
}
