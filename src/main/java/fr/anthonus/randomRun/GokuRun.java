package fr.anthonus.randomRun;

import fr.anthonus.utils.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

public class GokuRun extends RandomRun {
    private static final String tpSound = "/runAssets/goku/gokuTP.wav";

    public GokuRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        super(
                root,
                imagePath,
                soundPath,
                imageX,
                imageY,
                imageWitdh,
                imageHeight,
                imageOpacity
        );
    }

    @Override
    public void run() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        final int[] tpLeft = {rand.nextInt(10, 20)}; //tableau pour qu'il soit final pour fonctionner dans le listener

        imageView.setOnMouseEntered(_ -> {
            if (tpLeft[0] < 0) return;

            double randomX = rand.nextDouble(0, screenWidth-imageWitdh);
            double randomY = rand.nextDouble(0, screenHeight-imageHeight);

            imageView.setLayoutX(randomX);
            imageView.setLayoutY(randomY);
            imageX=randomX;
            imageY=randomY;

            MediaPlayer tpPlayer = Utils.createMediaPlayer(tpSound);
            tpPlayer.setVolume(0.2);
            players.add(tpPlayer);
            tpPlayer.play();
            tpPlayer.setOnEndOfMedia(tpPlayer::dispose);

            tpLeft[0]--;
        });

        addDeleteListener();

        MediaPlayer player = players.getFirst();
        player.setCycleCount(MediaPlayer.INDEFINITE);
        imageView.setOpacity(0);
        player.setVolume(0);

        player.setOnReady(() -> { //besoin pour la setDuration
            player.seek(Duration.seconds(50));

            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(imageView.opacityProperty(), 0.0),
                            new KeyValue(player.volumeProperty(), 0.0)
                    ),
                    new KeyFrame(Duration.seconds(40),
                            new KeyValue(imageView.opacityProperty(), 1.0),
                            new KeyValue(player.volumeProperty(), maxVolume)
                    )
            );

            fadeIn.setOnFinished(_ -> player.setCycleCount(MediaPlayer.INDEFINITE));

            root.getChildren().add(imageView);
            fadeIn.play();
            player.play();
        });
    }
}
