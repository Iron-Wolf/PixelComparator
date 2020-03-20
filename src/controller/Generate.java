package controller;

import Ressources.Data;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller use an Image as parameter and display a canvas with calculated hex color
 */
public class Generate implements Initializable {

    @FXML
    private ScrollPane center_scroll_pane;

    private Image selectedImage;

    /**
     * Initializes the objects needed by the controller and process them
     *
     * @param image image from which the generation is made
     */
    public void initData(Image image) {
        selectedImage = image;
        displayImage();
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert center_scroll_pane != null : "fx:id=\"center_scroll_pane\" was not injected: check your FXML file.";
    }

    /**
     * Display the selected image in a new canvas, once all elements are loaded
     */
    private void displayImage() {
        if (selectedImage != null) {
            // calculate the zoom multiplier
            var maxLength = Math.max(selectedImage.getWidth(),selectedImage.getHeight());
            // convert the length into the inverted proportional value in the range of 1 to 10
            var result = (int) convertRange(maxLength, new double[]{0, Data.maxWidth}, new double[]{10,1});

            // create canvas and add it to the scroll pane
            var canvas = new Canvas(selectedImage.getWidth() * result, selectedImage.getHeight() * result);
            var gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            center_scroll_pane.setContent(canvas);

            /*final PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
            final WritablePixelFormat<ByteBuffer> byteBgraInstance = PixelFormat.getByteBgraPreInstance();*/

            BufferedImage image = SwingFXUtils.fromFXImage(selectedImage, null);
            for (int j = 0; j < selectedImage.getHeight(); j++) {
                for (int i = 0; i < selectedImage.getWidth(); i++) {
                    var pixel = image.getRGB(i, j);
                    var nearestHexValue = ImageWorker.getNearestColor(pixel);
                    // cast color for javaFx
                    var c = Color.valueOf("#" + nearestHexValue);
                    gc.setFill(c);
                    // create the rectangle
                    gc.fillRect(i * result, j * result, result, result);
                }
            }
        }
    }

    /**
     * Scale a given number from one range to another (with percentage)
     * @param value the number to scale
     * @param range1 first range containing possible min and max of the value
     * @param range2 second range in which the value has to be scale
     */
    private double normalize(double value, double[] range1, double[] range2){
        var percent = (value - range1[0]) / (range1[1] - range1[0]);
        return percent * (range2[1] - range2[0]) + range2[0];
    }

    /**
     * Scale a given number from one range to another.<br/>
     * This method calculate the number in the second range, proportionally to his position in the first range.
     * @param value the number to scale
     * @param range1 first range containing possible min and max of the value
     * @param range2 second range in which the value has to be scale
     */
    private double convertRange(double value, double[] range1, double[] range2) {
        return (value - range1[0]) * (range2[1] - range2[0]) / (range1[1] - range1[0]) + range2[0];
    }


}
