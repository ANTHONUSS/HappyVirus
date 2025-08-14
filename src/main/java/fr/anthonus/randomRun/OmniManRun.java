package fr.anthonus.randomRun;

import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;

public class OmniManRun extends RandomRun {

    public OmniManRun(Pane root, String imagePath, String soundPath, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
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

        MediaPlayer player = players.getFirst();
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(maxVolume);

        root.getChildren().add(imageView);
        player.play();
    }
}
