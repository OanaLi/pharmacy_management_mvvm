package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        String fxmlPath = "/view/PharmacyView.fxml";
        URL fxmlLocation = Launcher.class.getResource(fxmlPath);

        try {
            System.out.println("Attempting to load FXML from: " + fxmlLocation);
            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root, 1200, 800);

            primaryStage.setMaximized(true);
            primaryStage.setTitle("Pharmacy Management");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println("Failed to load FXML from: " + fxmlLocation);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}