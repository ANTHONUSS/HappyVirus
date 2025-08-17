package fr.anthonus.randomRun.runs.omniMan;

import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.utils.Utils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class OmniManRun extends RandomRun {
    private static final Image omniManImage = Utils.createImage("/runAssets/omniMan/omniMan.png");
    private static final Media omniManSound = Utils.createMedia("/runAssets/omniMan/omniMan.mp3");

    public OmniManRun(Pane root, double imageX, double imageY, double imageWitdh, double imageHeight, double imageOpacity) {
        super(
                root,
                imageX,
                imageY,
                imageWitdh,
                imageHeight,
                imageOpacity
        );
    }

    @Override
    public void run() {
        ImageView omniManView = Utils.createImageView(omniManImage, imageX, imageY, imageWitdh, imageHeight, imageOpacity);
        imageViews.add(omniManView);
        addStopListener(omniManView);

        MediaPlayer omniManMediaPlayer = Utils.createMediaPlayer(omniManSound, maxVolume, true);
        mediaPlayers.add(omniManMediaPlayer);

        root.getChildren().add(omniManView);
        omniManMediaPlayer.play();
    }
}
