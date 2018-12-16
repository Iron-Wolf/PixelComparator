package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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


    private Canvas canvas;
    @FXML
    private ScrollPane center_scroll_pane;

    private Image selectedImage;

    /**
     * Initializes the objects needed by the controller and process them
     *
     * @param image
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
            // create canvas and add it to the scroll pane
            canvas = new Canvas(selectedImage.getWidth() * 10, selectedImage.getHeight() * 10);
            final GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            center_scroll_pane.setContent(canvas);

            /*final PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
            final WritablePixelFormat<ByteBuffer> byteBgraInstance = PixelFormat.getByteBgraPreInstance();*/

            BufferedImage image = SwingFXUtils.fromFXImage(selectedImage, null);
            for (int j = 0; j < selectedImage.getHeight(); j++) {
                for (int i = 0; i < selectedImage.getWidth(); i++) {
                    int pixel = image.getRGB(i, j);
                    String nearestHexValue = ImageWorker.getNearestColor(pixel);
                    Color c = Color.valueOf("#" + nearestHexValue);
                    gc.setFill(c);
                    // create a 10*10 rectangle
                    gc.fillRect(i * 10, j * 10, 10, 10);
                }
            }
        }
    }

}
