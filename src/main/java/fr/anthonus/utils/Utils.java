package fr.anthonus.utils;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;

public class Utils {

    public static ImageView createImage(String path, double x, double y, double w, double h, double o) {
        String url;
        File f = new File(path);
        if (f.exists()) {
            url = f.toURI().toString();
        } else {
            URL res = Utils.class.getResource(path.startsWith("/") ? path : "/" + path);
            if (res != null) {
                url = res.toExternalForm();
            } else {
                throw new IllegalArgumentException("Image non trouvée : " + path);
            }
        }

        double requiredWidth = (w > 0) ? w : 0;
        double requiredHeight = (h > 0) ? h : 0;
        Image img = new Image(url, requiredWidth, requiredHeight, true, true, true);

        ImageView imageView = new ImageView(img);

        if (w > 0) imageView.setFitWidth(w);
        if (h > 0) imageView.setFitHeight(h);
        imageView.setPreserveRatio(true);
        imageView.setOpacity(o);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setCursor(Cursor.HAND);

        imageView.setPickOnBounds(true);

        return imageView;
    }
}
