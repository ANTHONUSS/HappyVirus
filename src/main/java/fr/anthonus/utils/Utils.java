package fr.anthonus.utils;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;

public class Utils {

    public static Image createImage(String path) {
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

        return new Image(url, 0, 0, true, true, false);
    }

    public static ImageView createImageView(Image image, double x, double y, double w, double h, double o) {
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        imageView.setPreserveRatio(true);
        imageView.setOpacity(o);

        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setCursor(Cursor.HAND);

        imageView.setPickOnBounds(true);

        return imageView;
    }

    public static Media createMedia(String path) {
        String url;
        File f = new File(path);
        if (f.exists()) {
            url = f.toURI().toString();
        } else {
            URL res = Utils.class.getResource(path.startsWith("/") ? path : "/" + path);
            if (res != null) {
                url = res.toExternalForm();
            } else {
                throw new IllegalArgumentException("Son non trouvée : " + path);
            }
        }

        return new Media(url);
    }

    public static MediaPlayer createMediaPlayer(Media media, double volume, boolean infinitePlay) {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        if (infinitePlay) mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        else mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
        return mediaPlayer;
    }
}
