package fr.anthonus.randomRun.runs.amogus;

import fr.anthonus.randomRun.RandomRun;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.util.Duration;

public class AmogusRun extends RandomRun {

    public AmogusRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
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
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        Timeline showUp = new Timeline(
                new KeyFrame(Duration.seconds(0)),
                new KeyFrame(Duration.seconds(50),
                        new KeyValue(imageView.layoutYProperty(), screenHeight - imageHeight - 10)
                )
        );
        timelines.add(showUp);

        MediaPlayer player = players.getFirst();
        player.setCycleCount(Timeline.INDEFINITE);
        player.setVolume(maxVolume);

        root.getChildren().add(imageView);
        showUp.play();
        player.play();
    }
}
