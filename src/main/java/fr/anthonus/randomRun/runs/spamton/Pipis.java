package fr.anthonus.randomRun.runs.spamton;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

import java.util.List;

public class Pipis extends ImageView {
    private static final double size = 50;

    public Pipis(Image image, double x, double y, List<Pipis> allPipis) {
        super(image);
        setFitHeight(size);
        setPreserveRatio(true);

        setLayoutX(x);
        setLayoutY(y);

        final double[] dragOffset = new double[2];
        setOnMousePressed(event -> {
            // Calculer le décalage entre la position de la souris et le coin supérieur gauche de l'image
            dragOffset[0] = event.getSceneX() - getLayoutX();
            dragOffset[1] = event.getSceneY() - getLayoutY();
        });
        setOnMouseDragged(event -> {
            double newX = event.getSceneX() - dragOffset[0];
            double newY = event.getSceneY() - dragOffset[1];

            doCollision(this, newX, newY, allPipis);
        });
    }

    private void doCollision(Pipis dragged, double newX, double newY, List<Pipis> allPipis) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        dragged.setLayoutX(newX);
        dragged.setLayoutY(newY);

        for (Pipis other : allPipis) {
            if (other.getLayoutX() < 0) other.setLayoutX(0);
            if (other.getLayoutX() + Pipis.size > screenWidth) other.setLayoutX(screenWidth - Pipis.size);
            if (other.getLayoutY() < 0) other.setLayoutY(0);
            if (other.getLayoutY() + Pipis.size > screenHeight) other.setLayoutY(screenHeight - Pipis.size);

            if (other == dragged) continue;

            if (dragged.getBoundsInParent().intersects(other.getBoundsInParent())) {
                double dx = dragged.getLayoutX() - other.getLayoutX();
                double dy = dragged.getLayoutY() - other.getLayoutY();
                double distance = Math.sqrt(dx*dx + dy*dy);
                if (distance == 0) { dx = 1; dy = 1; distance = Math.sqrt(2); }
                double push = 5;
                other.setLayoutX(other.getLayoutX() - dx / distance * push);
                other.setLayoutY(other.getLayoutY() - dy / distance * push);
            }
        }
    }
}
