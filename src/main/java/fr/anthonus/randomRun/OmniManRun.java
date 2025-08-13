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
        //exemple d'image avec listener
        imageView.setOnMousePressed(_ -> {
            // Arrêter le son
            players.forEach(MediaPlayer::stop);
            // Supprimer l'image
            root.getChildren().remove(imageView);
        });

        root.getChildren().add(imageView);
        MediaPlayer player = players.getFirst();
        player.play();

    }
}
