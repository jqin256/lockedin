import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisualTimer extends Application {

    private int timeLeft; // seconds
    private Timeline timeline;
    private Label timeLabel;
    private TextField setTimeField;

    private Button startButton, pauseButton, resetButton, setButton, plusButton, minusButton;
    private BorderPane root;
    private WelcomeScreen welcomeScreen;
    private ArrayList<String> selectedApps = new ArrayList<String>();
    private HashMap<String, String> nameToPath;
    private LockList selectedLockList = new LockList();
    public VisualTimer(WelcomeScreen w) {
        welcomeScreen = w;
    }

    public VisualTimer() {}

    @Override
    public void start(Stage stage) {
        try {
            File f = new File("data\\locklist.txt");
            if (f.isFile()) {
                BufferedReader br = new BufferedReader(new FileReader("data\\locklist.txt"));
                String line = br.readLine();
                while (line != null) {
                    line = line.trim();
                    selectedApps.add(nameToPath.get(line));
                    line = br.readLine();
                }
                br.close();
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        selectedLockList.setLockList(selectedApps);


        /* ================= BACKGROUND ================= */

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#2193b0")),
                new Stop(1, Color.web("#6dd5ed"))
        );

        root = new BorderPane();
        root.setBackground(new Background(
                new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        /* ================= TIMER LABEL ================= */

        timeLabel = new Label("00:00");
        timeLabel.setFont(Font.font("Segoe UI Semibold", 90));
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setAlignment(Pos.CENTER);

        DropShadow glow = new DropShadow(25, Color.web("#ffffff80"));
        timeLabel.setEffect(glow);

        /* Pulse animation while running */
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.2), timeLabel);
        pulse.setFromX(1.0);
        pulse.setToX(1.04);
        pulse.setFromY(1.0);
        pulse.setToY(1.04);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);

        /* ================= TIMER LOGIC ================= */

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeLeft > 0) {
                timeLeft--;
                updateLabel();

                // Turn red when under 10 seconds
                if (timeLeft <= 10) {
                    timeLabel.setTextFill(Color.web("#ffcccc"));
                }
                selectedLockList.killProcesses();
            } 
            else {
                timeline.stop();
                pulse.stop();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Time's Up!");
                alert.setHeaderText(null);
                alert.setContentText("Time's Up!");
                alert.showAndWait();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        /* ================= BUTTON STYLING ================= */

        startButton = styledButton("Start", "#2ecc71");
        pauseButton = styledButton("Pause", "#f1c40f");
        resetButton = styledButton("Reset", "#e74c3c");

        startButton.setOnAction(e -> {
            if (timeLeft > 0 && timeline.getStatus() != Timeline.Status.RUNNING) {
                timeline.play();
                pulse.play();
            }
        });

        pauseButton.setOnAction(e -> {
            timeline.pause();
            pulse.pause();
        });

        resetButton.setOnAction(e -> {
            timeline.stop();
            pulse.stop();
            timeLeft = 0;
            updateLabel();
            timeLabel.setTextFill(Color.WHITE);
        });

        Button homeButton = styledButton("Home", "#3498db");
        homeButton.setOnAction(e -> welcomeScreen.start(stage));

        HBox controlBox = new HBox(12, homeButton, startButton, pauseButton, resetButton);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(15));

        /* ================= SET TIME ================= */

        Label setLabel = new Label("Set (sec):");
        setLabel.setTextFill(Color.WHITE);

        setTimeField = new TextField();
        setTimeField.setPrefWidth(90);

        setButton = styledButton("Set", "#1abc9c");

        setButton.setOnAction(e -> {
            try {
                timeLeft = Integer.parseInt(setTimeField.getText());
                updateLabel();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Enter a valid number").show();
            }
        });

        HBox setBox = new HBox(10, setLabel, setTimeField, setButton);
        setBox.setAlignment(Pos.CENTER);
        setBox.setPadding(new Insets(10));

        /* ================= ADJUST ================= */

        plusButton = styledButton("+1 min", "#16a085");
        minusButton = styledButton("-1 min", "#16a085");

        plusButton.setOnAction(e -> {
            timeLeft += 60;
            updateLabel();
        });

        minusButton.setOnAction(e -> {
            timeLeft = Math.max(0, timeLeft - 60);
            updateLabel();
        });

        VBox adjustBox = new VBox(12, plusButton, minusButton);
        adjustBox.setAlignment(Pos.CENTER_RIGHT);
        adjustBox.setPadding(new Insets(10));

        /* ================= LAYOUT ================= */

        root.setCenter(timeLabel);
        root.setBottom(controlBox);
        root.setTop(setBox);
        root.setRight(adjustBox);

        Scene scene = new Scene(root, 420, 300);
        stage.setTitle("LockedIn Timer");
        stage.setScene(scene);
        stage.show();
    }

    /* ================= HELPERS ================= */

    private Button styledButton(String text, String color) {
        Button b = new Button(text);
        b.setFont(Font.font("Segoe UI", 14));
        b.setTextFill(Color.WHITE);
        b.setPadding(new Insets(8, 18, 8, 18));

        b.setBackground(new Background(new BackgroundFill(
                Color.web(color), new CornerRadii(14), Insets.EMPTY)));

        DropShadow shadow = new DropShadow(10, Color.rgb(0, 0, 0, 0.3));
        b.setEffect(shadow);

        b.setOnMouseEntered(e -> scale(b, 1.05));
        b.setOnMouseExited(e -> scale(b, 1.0));

        return b;
    }

    private void scale(Button b, double v) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), b);
        st.setToX(v);
        st.setToY(v);
        st.play();
    }

    private void updateLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    public void setWelcomeScreen (WelcomeScreen w)
    {
        welcomeScreen = w;
    }
    public void setNameToPath(HashMap<String, String> a) {
        nameToPath = a;
    }
}