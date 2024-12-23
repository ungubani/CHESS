package view;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();

    public static ImageIcon getImage(String fileName) {
        if (imageCache.containsKey(fileName)) {
            return imageCache.get(fileName);
        }
        try {
            ImageIcon icon = new ImageIcon(ImageLoader.class.getResource("/resources/" + fileName));
            imageCache.put(fileName, icon);
            return icon;
        } catch (Exception e) {
            System.err.println("Error loading image: " + fileName);
            return null;
        }
    }
}
