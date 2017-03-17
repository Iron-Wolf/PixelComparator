package controller;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ImageWorker {
    private GridPane imageGPane, colorGPane;
    private static ArrayList<String> imageHexaColor;

    public ImageWorker(GridPane imageGPane, GridPane colorGPane){
        this.imageGPane = imageGPane;
        this.colorGPane = colorGPane;
    }

    /**
     * Retrieve pixel from image and display them on imageGPane
     * @param img
     */
    public void pixelProcess(Image img) {
        imageHexaColor = new ArrayList<>();
        // charge image to a buffer
        BufferedImage image = SwingFXUtils.fromFXImage(img,null);

        // fill the GridPane with colored Pane
        for (int j=0; j<img.getHeight(); j++) {

            // progress bar on the horizontal loop (row index)
            //Double progress = (j*100)/img.getHeight();
            //System.out.println("Load : " + progress.intValue()*0.01);

            for (int i = 0; i < img.getWidth(); i++) {
                Pane p = new Pane();
                p.setPrefSize(10, 10);
                // retrieve color from a pixel
                int pixel = image.getRGB(i, j);
                // set style with the rgba pixel color
                p.setStyle("-fx-background-color: " + formatARGB(pixel));
                imageGPane.add(p, i, j);
                // add the nearest corresponding color from the HashMap in the ArrayList
                addNearestColor(pixel);
            }
        }
        // other method
        /*PixelReader reader = img.getPixelReader();
        System.out.println("Color : "+reader.getColor(i,j));*/
    }


    /**
     * Iterate through the ArrayList and display the color on colorGPane
     */
    public void printColor() {
        for (int i=0; i<imageHexaColor.size(); i++) {
            JFXButton b = new JFXButton();
            b.setPrefSize(100,50);
            b.setStyle("-fx-background-color: #" + imageHexaColor.get(i));
            b.setText(Data.hmap.get(imageHexaColor.get(i)));
            colorGPane.add(b, 0, i++);
        }
    }


    /**
     * Add the nearest hexa value from {@link Data} to the Arraylist.<br/>
     * Values are not duplicate in the array.
     * @param pixel value to compare and add to the array
     */
    private void addNearestColor(int pixel){
        // build hexa value from int parameter
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        String hex = String.format("%02x%02x%02x", red, green, blue);

        // compare hexa value to our HashMap
        String nearestHexValue = compare(hex, Data.hmap);
        System.out.println("hex : " + hex + " / nearest : "+nearestHexValue + " / name : "+Data.hmap.get(nearestHexValue));

        // add to list if not already in
        if (!imageHexaColor.contains(nearestHexValue))
            imageHexaColor.add(nearestHexValue);
    }


    /**
     * Return the nearest value from a HashMap to a given value
     * @param hex
     * @param hmap
     * @return
     */
    private String compare(String hex, HashMap hmap){
        int rgb1 = Integer.parseInt(hex, 16); int red,green,blue; double answer,ref=1;
        String keyResult = "";

        Iterator it = hmap.entrySet().iterator();
        while (it.hasNext()) {
            // iterate through colors
            HashMap.Entry pair = (HashMap.Entry) it.next();

            // retrieve current hexa value (from the hashmap) as an Integer
            int rgb2 = Integer.parseInt(((String)pair.getKey()).toLowerCase(),16);

            // calculate the distance between the given color and current color
            red = (rgb1 >> 16) & 0xff;
            green = (rgb1 >> 8) & 0xff;
            blue = (rgb1) & 0xff;

            red -= (rgb2 >> 16) & 0xff;
            green -= (rgb2 >> 8) & 0xff;
            blue -= (rgb2) & 0xff;

            answer = (red*red+green*green+blue*blue)/(255*255*3.0);

            // the answer is a value between 0 and 1 :
            // 0 = the two color are identical
            // 1 = the colors are opposite (black / white)
            if (answer<ref) {
                ref = answer;
                keyResult = (String)pair.getKey();
            }

        }

        return keyResult;
    }

    /**
     * Format an integer color value to an RGBA value
     * @param pixel integer color
     * @return RGBA formated value as a String
     */
    private String formatARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        // change the alpha value to a percentage of 255
        int perc = (alpha * 100)/255;
        // format the integer to a 0.00 format
        double result = perc*0.01;

        return "rgba("+red+","+green+","+blue+","+result+")";
    }
}
