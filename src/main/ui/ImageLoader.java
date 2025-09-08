package ui;

import javax.swing.*;
import java.awt.*;

// Loads images from a directory and resize them to the specified size
public class ImageLoader {

    // EFFECTS: Loads an image from a directory and resizes it to the given width and height.
    public static ImageIcon loadIcon(String fileName, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(ImageLoader.class.getResource("/data/" + fileName));
        return scaleImage(originalIcon, width, height);
    }

    // EFFECTS: resize an ImageIcon to the specified width and height
    private static ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage(); 
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
