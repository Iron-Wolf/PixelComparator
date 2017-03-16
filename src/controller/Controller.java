package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import main.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class Controller implements Initializable {
    @FXML
    private MenuItem menuitem_file_open, menuitem_file_close, menuitem_help_about;
    @FXML
    private ScrollPane scroll_pane;
    @FXML
    private GridPane grid_pane;

    private static String fileChooserPath = "";


    @Override // method called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // check @FXML variables before use them
        assert menuitem_file_open != null : "fx:id=\"menuitem_file_open\" was not injected: check your FXML file.";
        assert menuitem_file_close != null : "fx:id=\"menuitem_file_close\" was not injected: check your FXML file.";
        assert menuitem_help_about != null : "fx:id=\"menuitem_file_about\" was not injected: check your FXML file.";


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
                pixelProcess(image);
            }
        });

        menuitem_file_close.setOnAction(event -> Platform.exit());


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


    public void pixelProcess(Image img) {
        // charge image to a buffer
        BufferedImage image = SwingFXUtils.fromFXImage(img,null);

        // fill the GridPane with colored Pane
        for (int j=0; j<img.getHeight(); j++) {

            // progress bar on the horizontal loop (row index)
            Double progress = (j*100)/img.getHeight();
            System.out.println("Load : " + progress.intValue());

            for (int i = 0; i < img.getWidth(); i++) {
                Pane l = new Pane();
                l.setPrefSize(10, 10);
                // retrieve color from a pixel
                int pixel = image.getRGB(i, j);
                // dynamicaly set style with the rgba pixel color
                l.setStyle("-fx-background-color: " + formatARGB(pixel));
                grid_pane.add(l, i, j);
            }
        }

        // other method
        /*PixelReader reader = img.getPixelReader();
        System.out.println("Color : "+reader.getColor(i,j));*/
    }

    public String formatARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        // change the alpha value to a percentage of 255
        int perc = (alpha * 100)/255;
        // format the integer to a 0.00 format
        double result = perc*0.01;

        //String hex = String.format("#%02x%02x%02x", red, green, blue);
        String hex = "rgba("+red+","+green+","+blue+","+result+")";
        return hex;
    }
}
