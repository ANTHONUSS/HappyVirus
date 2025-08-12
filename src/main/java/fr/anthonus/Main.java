package fr.anthonus;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URISyntaxException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws URISyntaxException {
        Pane root = new Pane();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMaximized(true);
        stage.setAlwaysOnTop(true);

        Scene scene = new Scene(root, Color.TRANSPARENT);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args)  {
        launch(args);
    }

}