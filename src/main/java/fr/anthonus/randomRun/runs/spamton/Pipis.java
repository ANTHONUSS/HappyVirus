package fr.anthonus.randomRun.runs.spamton;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pipis extends ImageView {
    public static final double size = 50;

    public Pipis(Image image, double x, double y) {
        super(image);
        setFitHeight(size);
        setPreserveRatio(true);
        setCache(true);
        setPickOnBounds(true);

        setLayoutX(x);
        setLayoutY(y);

        final double[] dragOffset = new double[2];
        setOnMousePressed(event -> {
            // Calculer le décalage entre la position de la souris et le coin supérieur gauche de l'image
            dragOffset[0] = event.getSceneX() - getLayoutX();
            dragOffset[1] = event.getSceneY() - getLayoutY();
        });
        setOnMouseDragged(event -> {
            // Déplacer l'imageView en suivant la souris tout en respectant le décalage
            setLayoutX(event.getSceneX() - dragOffset[0]);
            setLayoutY(event.getSceneY() - dragOffset[1]);
        });

    }
}
