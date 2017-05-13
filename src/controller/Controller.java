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

import java.awt.*;
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

        center_grid_pane.setOnMouseClicked( e -> {
            try {
                // get the pixel under the mouse
                Robot robot = new Robot();
                Point coord = MouseInfo.getPointerInfo().getLocation();
                Color color = robot.getPixelColor((int) coord.getX(), (int) coord.getY());

                // format in hexadecimal and compare it with predefined color
                String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                String value = Data.hmap.get(ImageWorker.compare(hex, Data.hmap));

                // display color in the title (because why not)
                Main.getPrimaryStage().setTitle(Data.appName + " - " + value + " (" + hex.toUpperCase() + ")");
            } catch (AWTException e1) {
            }
        });

        // MENU ITEMS
        menuitem_file_open.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(Data.appName + " - Select file");
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
                // test size of the image
                if (image.getHeight() <= 400 || image.getWidth() <= 400) {
                    imageWorker.pixelProcess(image);
                    imageWorker.printColor();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(Data.appName + " - Warning");
                    alert.setContentText("Image supérieure à 400x400.");
                    // setting the modality and owner will assure that the alert will appear over the stage,
                    // even if the stage is moved
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(Main.getPrimaryStage());
                    alert.showAndWait();
                }
            }
        });

        menuitem_file_close.setOnAction(event -> Platform.exit());


        menuitem_edit_delete.setOnAction(event -> {
            imageWorker.clearGridPane(center_grid_pane);
            imageWorker.clearGridPane(right_grid_pane);
        });


        menuitem_help_about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle(Data.appName + " - About");
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
