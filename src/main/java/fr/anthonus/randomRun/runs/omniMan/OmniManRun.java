package fr.anthonus.randomRun.runs.omniMan;

import fr.anthonus.randomRun.RandomRun;
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
        addDeleteListener(this);

        MediaPlayer player = players.getFirst();
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(maxVolume);

        root.getChildren().add(imageView);
        player.play();
    }
}
