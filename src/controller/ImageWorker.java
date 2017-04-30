package controller;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImageWorker {
    private GridPane imageGPane, colorGPane;
    private static HashMap<String,Integer> imageHexaColor;// contains the hexadecimal code and the number of occurrences

    public ImageWorker(GridPane imageGPane, GridPane colorGPane) {
        this.imageGPane = imageGPane; // image canvas
        this.colorGPane = colorGPane; // button area
    }

    /**
     * Retrieve pixel from image and display them on imageGPane
     *
     * @param img the image object
     */
    public void pixelProcess(Image img) {
        imageHexaColor = new HashMap<>();
        // charge image to a buffer
        BufferedImage image = SwingFXUtils.fromFXImage(img, null);

        // fill the GridPane with colored Pane
        for (int j = 0; j < img.getHeight(); j++) {
            // progress bar on the horizontal loop (row index)
            //Double progress = (j*100)/img.getHeight();
            //System.out.println("Load : " + progress.intValue()*0.01);

            for (int i = 0; i < img.getWidth(); i++) {
                Pane p = new Pane();
                p.setPrefSize(10, 10);
                // retrieve color from a pixel
                int pixel = image.getRGB(i, j);

                // get the nearest corresponding color from the HashMap reference in the imageHexaColor map
                String nearestHexValue = getNearestColor(pixel);
                // add to list if not already in and increment the counter
                imageHexaColor.putIfAbsent(nearestHexValue, 0);
                imageHexaColor.replace(nearestHexValue, imageHexaColor.get(nearestHexValue) + 1);

                // set style with the rgba pixel color
                p.setStyle("-fx-background-color: " + formatARGB(pixel));
                //p.setOnMouseEntered(e -> p.setStyle("-fx-border-color: black"));
                //p.setOnMouseExited(e -> p.setStyle("-fx-background-color: " + formatARGB(pixel)));
                // add a tooltip with the color name
                //Tooltip.install(p, new Tooltip(Data.hmap.get(nearestHexValue)));
                imageGPane.add(p, i, j);
            }
        }
        // other method
        /*PixelReader reader = img.getPixelReader();
        System.out.println("Color : "+reader.getColor(i,j));*/
    }


    /**
     * Iterate through the HashMap and display the color on colorGPane
     */
    public void printColor() {
        int i = 0;
        // iterate through colors
        for (HashMap.Entry pair : imageHexaColor.entrySet()) {
            JFXButton b = new JFXButton();
            b.setPrefSize(140, 20);
            b.setMnemonicParsing(false);

            // display white text on dark background
            String c = distance(pair.getKey().toString(), "000000") < 0.50 ? "white" : "black";
            b.setStyle("-fx-background-color: #" + pair.getKey().toString() +
                    ";-fx-text-fill: " + c);
            b.setText(Data.hmap.get(pair.getKey().toString()) + " - " + pair.getValue());

            colorGPane.add(b, 0, i++);
        }
    }


    /**
     * Return the nearest hexa value from {@link Data}.<br/>
     *
     * @param pixel value to compare
     * @return the nearest value from the HashMap
     */
    private String getNearestColor(int pixel) {
        // build hexa value from int parameter
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        String hex = String.format("%02x%02x%02x", red, green, blue);

        // compare hexa value to the HashMap
        return compare(hex, Data.hmap);
    }


    /**
     * Compare the nearest value from a HashMap to a given value
     *
     * @param hex  value to compare
     * @param hmap HashMap containing all the values to be compared
     * @return the matching value from the HashMap
     */
    public static String compare(String hex, HashMap<String,String> hmap) {
        double answer, ref = 1;
        String keyResult = "";
        // iterate through HashMap
        for (HashMap.Entry pair : hmap.entrySet()) {
            // compare the given value and the current HashMap key
            answer = distance(hex, pair.getKey().toString().toLowerCase());

            if (answer < ref) {
                ref = answer;
                keyResult = (String) pair.getKey();
            }
        }
        return keyResult;
    }

    /**
     * calculates a euclidean distance and scales it to range from 0 to 1.
     *
     * @param hexVal value to compare (in "ffffff" format)
     * @param hexRef reference value to be compared (in "ffffff" format)
     * @return a value between 0 (identical) and 1 (opposite)
     */
    private static double distance(String hexVal, String hexRef) {
        int red, green, blue;
        double answer;
        //convert values to int
        int rgbVal = Integer.parseInt(hexVal, 16);
        int rgbRef = Integer.parseInt(hexRef, 16);

        // calculate the distance between the given color and current color
        red = (rgbVal >> 16) & 0xff;
        green = (rgbVal >> 8) & 0xff;
        blue = (rgbVal) & 0xff;

        red -= (rgbRef >> 16) & 0xff;
        green -= (rgbRef >> 8) & 0xff;
        blue -= (rgbRef) & 0xff;

        answer = (red * red + green * green + blue * blue) / (255 * 255 * 3.0);

        // the answer is a value between 0 and 1 :
        // 0 = the two color are identical
        // 1 = the colors are opposite (black / white)
        return answer;
    }

    /**
     * Format an integer color value to an RGBA value
     *
     * @param pixel integer color
     * @return RGBA formated value as a String
     */
    private String formatARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        // change the alpha value to a percentage of 255
        int perc = (alpha * 100) / 255;
        // format the integer to a 0.00 format
        double result = perc * 0.01;

        return "rgba(" + red + "," + green + "," + blue + "," + result + ")";
    }
}
