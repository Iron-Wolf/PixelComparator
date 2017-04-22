package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import main.Main;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MenuItem menuitem_file_open, menuitem_file_close, menuitem_edit_delete, menuitem_help_about;
    @FXML
    private ScrollPane center_scroll_pane, right_scroll_pane;
    @FXML
    private GridPane center_grid_pane, right_grid_pane;

    private static String fileChooserPath = "";


    @Override // method called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // check @FXML variables before use them
        assert menuitem_file_open != null : "fx:id=\"menuitem_file_open\" was not injected: check your FXML file.";
        assert menuitem_file_close != null : "fx:id=\"menuitem_file_close\" was not injected: check your FXML file.";
        assert menuitem_edit_delete != null : "fx:id=\"menuitem_edit_delete\" was not injected: check your FXML file.";
        assert menuitem_help_about != null : "fx:id=\"menuitem_file_about\" was not injected: check your FXML file.";


        ImageWorker imageWorker = new ImageWorker(center_grid_pane, right_grid_pane);

        // MENU ITEMS
        menuitem_file_open.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(Main.getPrimaryStage().getTitle() + " - Select file");
            // open FileChooser with the last know location (default is the home directory)
            fileChooserPath = fileChooserPath.equals("") ? System.getProperty("user.home") : fileChooserPath;
            chooser.setInitialDirectory(new File(fileChooserPath));

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = chooser.showOpenDialog(Main.getPrimaryStage());

            if (file != null) {
                fileChooserPath = file.getParent();
                Image image = new Image(file.toURI().toString());
                imageWorker.pixelProcess(image);
                imageWorker.printColor();
            }
        });

        menuitem_file_close.setOnAction(event -> Platform.exit());


        menuitem_edit_delete.setOnAction(event -> {
            //center_grid_pane
        });


        menuitem_help_about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle(Main.getPrimaryStage().getTitle() + " - About");
            alert.setHeaderText("Pixel Comparator\nVersion : Alpha");
            alert.setContentText("Runtime : "
                    + "\nJRE : "+System.getProperty("java.runtime.version")
                    + "\nJVM : "+System.getProperty("os.arch"));

            // setting the modality and owner will assure that the alert will appear over the stage,
            // even if the stage is moved
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(Main.getPrimaryStage());
            alert.showAndWait();
        });

    }


    /*public void updateProgress(double progress){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progress_bar.progressProperty(), progress)),
                new KeyFrame(Duration.seconds(0.01), new KeyValue(progress_bar.progressProperty(), progress)));

        //timeline.playFrom(new Duration());
        //timeline.setCycleCount(1);
        timeline.play();
    }*/
}
