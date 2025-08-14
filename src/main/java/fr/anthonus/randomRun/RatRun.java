package fr.anthonus.randomRun;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.concurrent.ThreadLocalRandom;

public class RatRun extends RandomRun {

    public RatRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
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
        addDeleteListener();

        double bpm = 113/4.0;
        double intervalSeconds = 60.0 / bpm;
        double offsetSeconds = 0.2;

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        Timeline bpmTimer = new Timeline(
                new KeyFrame(Duration.seconds(intervalSeconds), _ -> {
                    double randomX = rand.nextDouble(0, screenWidth-imageWitdh);
                    double randomY = rand.nextDouble(0, screenHeight-imageHeight);

                    imageView.setLayoutX(randomX);
                    imageView.setLayoutY(randomY);
                    imageX=randomX;
                    imageY=randomY;
                })
        );
        bpmTimer.setCycleCount(Timeline.INDEFINITE);
        bpmTimer.setDelay(Duration.seconds(offsetSeconds));

        MediaPlayer player = players.getFirst();
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(maxVolume);

        root.getChildren().add(imageView);
        bpmTimer.play();
        player.play();
    }
}
