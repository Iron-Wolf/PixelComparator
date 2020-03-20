package controller;

import Ressources.Data;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.robot.Robot;
import main.Main;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    @FXML
    private MenuItem menuitem_file_open, menuitem_file_generate, menuitem_file_close,
            menuitem_edit_delete,
            menuitem_help_about;
    @FXML
    private ScrollPane center_scroll_pane, right_scroll_pane;
    @FXML
    private GridPane center_grid_pane, right_grid_pane;

    private static String fileChooserPath = "";
    private static Image selectedImage;
    private JFXButton selectedButton;
    private Pane selectedPane;


    @Override // method called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // check @FXML variables before use them
        assert menuitem_file_open != null : "fx:id=\"menuitem_file_open\" was not injected: check your FXML file.";
        assert menuitem_file_generate != null : "fx:id=\"menuitem_file_generate\" was not injected: check your FXML file.";
        assert menuitem_file_close != null : "fx:id=\"menuitem_file_close\" was not injected: check your FXML file.";
        assert menuitem_edit_delete != null : "fx:id=\"menuitem_edit_delete\" was not injected: check your FXML file.";
        assert menuitem_help_about != null : "fx:id=\"menuitem_file_about\" was not injected: check your FXML file.";

        menuitem_file_generate.setDisable(true);
        var imageWorker = new ImageWorker(center_grid_pane, right_grid_pane);

        // Click on the main GridPane
        center_grid_pane.setOnMouseClicked(e -> {
            try {
                // get the pixel under the mouse
                var robot = new Robot();
                var coord = MouseInfo.getPointerInfo().getLocation();
                var color = robot.getPixelColor(coord.getX(),coord.getY());

                // format in hexadecimal
                var red = (int) Math.round(color.getRed() * 255.0D);
                var green = (int) Math.round(color.getGreen() * 255.0D);
                var blue = (int) Math.round(color.getBlue() * 255.0D);
                String hex = String.format("%02x%02x%02x",red, green, blue);

                // compare value with predefined color
                String value = Data.hmap.get(ImageWorker.compare(hex, Data.hmap));

                // display color in the title (because why not)
                Main.getPrimaryStage().setTitle(Data.appName + " - " + value + " (" + hex.toUpperCase() + ")");

                // select Pane on which the mouse click has been fired and add a border
                /*if (selectedPane != null)
                    selectedPane.setBorder(null);
                selectedPane = (Pane)e.getTarget();
                selectedPane.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.RED,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));*/

                // select corresponding button on the right grid pane
                for (int i = 0; i < right_grid_pane.getChildren().size(); i++) {
                    if (right_grid_pane.getChildren().get(i) instanceof JFXButton) {
                        JFXButton button = (JFXButton) right_grid_pane.getChildren().get(i);
                        // retrieve the button with his text (delimited by the dash char)
                        if (button.getText().contains(value + " -")) {
                            // remove border from previously selected button
                            if (selectedButton != null) {
                                selectedButton.setStyle(selectedButton.getStyle()
                                        .replace(";-fx-font-weight: bold;-fx-border-width: 3;-fx-border-color: red;", ""));
                            }
                            // save current button
                            selectedButton = button;
                            selectedButton.setStyle(button.getStyle() +
                                    ";-fx-font-weight: bold;-fx-border-width: 3;-fx-border-color: red;");
                            selectedButton.toFront();
                            break;
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // MENU ITEMS
        menuitem_file_open.setOnAction(event -> {
            var chooser = new FileChooser();
            chooser.setTitle(Data.appName + " - Select file");
            // open FileChooser with the last know location (default is the home directory)
            fileChooserPath = fileChooserPath.equals("") ? System.getProperty("user.home") : fileChooserPath;
            chooser.setInitialDirectory(new File(fileChooserPath));

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            var file = chooser.showOpenDialog(Main.getPrimaryStage());

            if (file != null) {
                fileChooserPath = file.getParent();
                selectedImage = new Image(file.toURI().toString());
                // test size of the image
                if (selectedImage.getHeight() <= Data.maxHeight || selectedImage.getWidth() <= Data.maxWidth) {
                    imageWorker.pixelProcess(selectedImage);
                    imageWorker.printColor();
                    menuitem_file_generate.setDisable(false);
                }
                else {
                    var alert = new Alert(Alert.AlertType.WARNING);
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

        menuitem_file_generate.setOnAction(event -> {
            try {
                if (selectedImage != null) {
                    var loader = new FXMLLoader(getClass().getResource("/view/generate.fxml"));
                    Parent root = loader.load();

                    // send the selected image to the controller
                    Generate generateController = loader.getController();
                    generateController.initData(selectedImage);

                    Stage stage = new Stage();
                    stage.setTitle(Data.appName + " - Generate");
                    stage.setScene(new Scene(root));
                    stage.show();
                    // Hide this current window (if this is what you want)
                    //((Node)(event.getSource())).getScene().getWindow().hide();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuitem_file_close.setOnAction(event -> Platform.exit());


        menuitem_edit_delete.setOnAction(event -> {
            imageWorker.clearGridPane(center_grid_pane);
            imageWorker.clearGridPane(right_grid_pane);
            menuitem_file_generate.setDisable(true);
            selectedImage = null;
        });


        menuitem_help_about.setOnAction(event -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setResizable(true);
            alert.setTitle(Data.appName + " - About");
            alert.setHeaderText("Pixel Comparator\nVersion : Alpha");
            alert.setContentText("This app allow importing image of 400 by 400 pixels" +
                    "\n\nCurrent Runtime : "
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
