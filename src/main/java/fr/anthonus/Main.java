package fr.anthonus;

import fr.anthonus.randomRun.RandomRun;
import fr.anthonus.randomRun.runs.amogus.AmogusRun;
import fr.anthonus.randomRun.runs.goku.GokuRun;
import fr.anthonus.randomRun.runs.omniMan.OmniManRun;
import fr.anthonus.randomRun.runs.rat.RatRun;
import fr.anthonus.randomRun.runs.spamton.SpamtonRun;
import fr.anthonus.randomRun.runs.tenna.TennaRun;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {
    public static boolean activated = true;
    public static boolean blocked = false;
    public static Thread whileTrueThread;
    private static int probabilite = 5;
    private static Pane root;

    public static final List<RandomRun> activeRuns = new ArrayList<>();

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
                if (!activated || blocked) {
                    System.out.println("bloqué");
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException _) {
                        return;
                    }
                    continue;
                }

                double randomChance = rand.nextDouble(0, 100);

                System.out.println("Random chance: " + randomChance + "%");
                if (randomChance < probabilite) createRandomRun(rand.nextInt(0, 6));

                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException _) {
                    return;
                }
            }
        });

        whileTrueThread.start();
    }

    private static void createPopupMenu() throws AWTException {
        PopupMenu popup = new PopupMenu();

        // Ajout du menu pour forcer l'apparition des items
        Menu runMenu = getRunMenu();

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

        MenuItem probabilityItem = new MenuItem("Changer la probabilité...");
        probabilityItem.addActionListener(action -> {
            // Fenêtre centrée
            JDialog dialog = new JDialog((Frame) null, "Définir la probabilité", true);
            dialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            // Spinner : valeur de 1 à 100
            SpinnerNumberModel model = new SpinnerNumberModel(probabilite, 1, 100, 1);
            JSpinner spinner = new JSpinner(model);

            // Bouton OK
            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    probabilite = (Integer) spinner.getValue();
                    System.out.println("Nouvelle probabilité : " + probabilite + "%");
                    dialog.dispose();
                }
            });

            // Placement dans la fenêtre
            gbc.gridx = 0;
            gbc.gridy = 0;
            dialog.add(new JLabel("Probabilité (1-100) :"), gbc);
            gbc.gridx = 1;
            dialog.add(spinner, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            dialog.add(okButton, gbc);

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        });
        popup.add(probabilityItem);

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

    private static Menu getRunMenu() {
        Menu runMenu = new Menu("Lancer un run");

        // Ajout de l'item pour le OmniManRun
        MenuItem omniManRun = new MenuItem("Omni Man");
        runMenu.add(omniManRun);

        // Ajout de l'item pour le GokuRun
        MenuItem gokuRunItem = new MenuItem("Goku");
        runMenu.add(gokuRunItem);

        // Ajout de l'item pour le RatRun
        MenuItem ratRunItem = new MenuItem("Rat");
        runMenu.add(ratRunItem);

        // Ajout de l'item pour le AmogusRun
        MenuItem amogusRunItem = new MenuItem("Amogus");
        runMenu.add(amogusRunItem);

        // Ajout de l'item pour le TennaRun
        MenuItem tennaRunItem = new MenuItem("Tenna");
        runMenu.add(tennaRunItem);

        // Ajout de l'item pour le TennaRun
        MenuItem spamtonRunItem = new MenuItem("Spamton");
        runMenu.add(spamtonRunItem);

        // ajout des listeners aux items
        for (int i = 0; i < runMenu.getItemCount(); i++) {
            final int type = i;
            runMenu.getItem(i).addActionListener(_ -> createRandomRun(type));
        }

        return runMenu;
    }

    private static void createRandomRun(int type) {
        if (blocked) return;

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        double randomSize = rand.nextDouble(150, 250);
        double randomX = rand.nextDouble(0, screenWidth - randomSize);
        double randomY = rand.nextDouble(0, screenHeight - randomSize);

        Platform.runLater(() -> {
            switch (type) {
                case 0 -> {
                    double newRandomSize = rand.nextDouble(150, 200);
                    OmniManRun omniman = new OmniManRun(
                            root,
                            "/runAssets/omniMan/omniMan.png",
                            "/runAssets/omniMan/omniMan.wav",
                            randomX,
                            randomY,
                            newRandomSize,
                            newRandomSize,
                            0.05
                    );
                    omniman.run();
                    activeRuns.add(omniman);
                }
                case 1 -> {
                    GokuRun goku = new GokuRun(
                            root,
                            "/runAssets/goku/goku.png",
                            "/runAssets/goku/goku.wav",
                            randomX,
                            randomY,
                            randomSize,
                            randomSize,
                            0
                    );
                    goku.run();
                    activeRuns.add(goku);
                }
                case 2 -> {
                    RatRun rat = new RatRun(
                            root,
                            "/runAssets/rat-dance/rat-dance.gif",
                            "/runAssets/rat-dance/rat-dance.wav",
                            randomX,
                            randomY,
                            randomSize,
                            randomSize,
                            1
                    );
                    rat.run();
                    activeRuns.add(rat);
                }
                case 3 -> {
                    AmogusRun amogus = new AmogusRun(
                            root,
                            "/runAssets/amogus/amogus.png",
                            "/runAssets/amogus/amogus.wav",
                            randomX,
                            screenHeight,
                            randomSize,
                            randomSize,
                            1
                    );
                    amogus.run();
                    activeRuns.add(amogus);
                }
                case 4 -> {
                    if (!activeRuns.isEmpty()) return;

                    TennaRun tenna = new TennaRun(
                            root,
                            "/runAssets/tenna/tennaRun.gif",
                            "/runAssets/tenna/running.wav",
                            randomX,
                            randomY,
                            0,
                            0,
                            1
                    );
                    tenna.run();
                    activeRuns.add(tenna);
                }
                case 5 -> {
                    SpamtonRun spamton = new SpamtonRun(
                            root,
                            "/runAssets/spamton/spamton_angel.gif",
                            "/runAssets/spamton/spamton.mp3",
                            screenWidth,
                            rand.nextDouble(500, screenHeight - 500),
                            250,
                            250,
                            1
                    );
                    spamton.run();
                    activeRuns.add(spamton);
                }
            }
        });
    }

}