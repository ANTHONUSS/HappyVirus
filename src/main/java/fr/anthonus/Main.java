package fr.anthonus;

import fr.anthonus.randomRun.*;
import fr.anthonus.randomRun.tenna.TennaRun;
import fr.anthonus.utils.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {
    public static boolean activated = true;
    public static Thread whileTrueThread;
    private static Pane root;

    @Override
    public void start(Stage ignored) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        Stage hiddenOwner = new Stage(StageStyle.UTILITY);
        hiddenOwner.setOpacity(0);
        hiddenOwner.setWidth(1);
        hiddenOwner.setHeight(1);
        hiddenOwner.setX(bounds.getMinX());
        hiddenOwner.setY(bounds.getMinY());
        hiddenOwner.show();

        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initOwner(hiddenOwner);
        stage.setAlwaysOnTop(true);
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());


        root = new Pane();
        Scene scene = new Scene(root, Color.TRANSPARENT);
        stage.setScene(scene);

        stage.show();

        try {
            createPopupMenu();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        whileTrue();

        launch(args);
    }

    public static void whileTrue() {
        whileTrueThread = new Thread(() -> {
            ThreadLocalRandom rand = ThreadLocalRandom.current();

            while (true) {
                if (!activated) {
                    return;
                }
                int randomTime = rand.nextInt(30_000, 180_000); // temps aléatoire entre 30 secondes et 3 minutes
                int totalSeconds = randomTime / 1000;
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                System.out.println("Next run in " + minutes + " minutes and " + seconds + " seconds");
                try {
                    Thread.sleep(randomTime);
                } catch (InterruptedException _) {
                    return;
                }

                createRun(rand.nextInt(2));
            }
        });

        whileTrueThread.start();
    }

    private static void createPopupMenu() throws AWTException {
        PopupMenu popup = new PopupMenu();

        // Ajout du menu pour forcer l'apparition des items
        Menu runMenu = new Menu("Lancer un run");

        // Ajout de l'item pour le OmniManRun
        MenuItem omniManRun = new MenuItem("Omni Man");
        omniManRun.addActionListener(e -> createRun(0));
        runMenu.add(omniManRun);

        // Ajout de l'item pour le GokuRun
        MenuItem gokuRunItem = new MenuItem("Goku");
        gokuRunItem.addActionListener(e ->  createRun(1));
        runMenu.add(gokuRunItem);

        // Ajout de l'item pour le RatRun
        MenuItem ratRunItem = new MenuItem("Rat");
        ratRunItem.addActionListener(e -> createRun(2));
        runMenu.add(ratRunItem);

        // Ajout de l'item pour le AmogusRun
        MenuItem amogusRunItem = new MenuItem("Amogus");
        amogusRunItem.addActionListener(e -> createRun(3));
        runMenu.add(amogusRunItem);

        // Ajout de l'item pour le TennaRun
        MenuItem tennaRunItem = new MenuItem("Tenna");
        tennaRunItem.addActionListener(e -> createRun(4));
        runMenu.add(tennaRunItem);

        // Ajout du menu au popup
        popup.add(runMenu);

        // Ajout de l'état d'activation
        CheckboxMenuItem checkbox = new CheckboxMenuItem("Activé");
        checkbox.setState(true);
        checkbox.addItemListener(_ -> {
            if (checkbox.getState()) {
                activated = true;
                whileTrue();
                System.out.println("Activated");
            } else {
                activated = false;
                if (whileTrueThread != null && whileTrueThread.isAlive()) {
                    whileTrueThread.interrupt();
                }
                System.out.println("Deactivated");
            }
        });
        popup.add(checkbox);

        popup.addSeparator();

        // Ajout de l'exit
        MenuItem exitItem = new MenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        popup.add(exitItem);

        Image icon = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/runAssets/omniMan/omniMan.png"));
        TrayIcon trayIcon = new TrayIcon(icon, "HappyVirus", popup);
        trayIcon.setImageAutoSize(true);


        SystemTray tray = SystemTray.getSystemTray();
        tray.add(trayIcon);
    }

    private static void createRun(int type) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        double randomSize = rand.nextDouble(150, 250);

        double randomX = rand.nextDouble(0, screenWidth-randomSize);
        double randomY = rand.nextDouble(0, screenHeight-randomSize);


        Platform.runLater(() -> {
            switch (type) {
                case 0 -> {
                    double newRandomSize = rand.nextDouble(150, 200);
                    new OmniManRun(
                            root,
                            "/runAssets/omniMan/omniMan.png",
                            "/runAssets/omniMan/omniMan.wav",
                            randomX,
                            randomY,
                            newRandomSize,
                            newRandomSize,
                            0.05
                    ).run();
                }
                case 1 -> {
                    new GokuRun(
                            root,
                            "/runAssets/goku/goku.png",
                            "/runAssets/goku/goku.wav",
                            randomX,
                            randomY,
                            randomSize,
                            randomSize,
                            0
                    ).run();
                }
                case 2 -> {
                    new RatRun(
                            root,
                            "/runAssets/rat-dance/rat-dance.gif",
                            "/runAssets/rat-dance/rat-dance.wav",
                            randomX,
                            randomY,
                            randomSize,
                            randomSize,
                            1
                    ).run();
                }
                case 3 -> {
                    new AmogusRun(
                            root,
                            "/runAssets/amogus/amogus.png",
                            "/runAssets/amogus/amogus.wav",
                            randomX,
                            screenHeight,
                            randomSize,
                            randomSize,
                            1
                    ).run();
                }
                case 4 -> {
                    try {
                        new TennaRun(
                                root,
                                "/runAssets/tenna/tennaRun.gif",
                                "/runAssets/tenna/running.wav",
                                randomX,
                                randomY,
                                0,
                                0,
                                1
                        ).run();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

}