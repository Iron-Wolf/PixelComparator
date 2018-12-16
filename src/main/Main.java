package main;

import Ressources.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainWindow.fxml"));

        pStage = primaryStage;
        pStage.setTitle(Data.appName);
        // specify the initial width and height, otherwise the content does not scall up to the window
        pStage.setScene(new Scene(root, 600, 400));
        pStage.setMinHeight(400);
        pStage.setMinWidth(600);
        pStage.show();
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
