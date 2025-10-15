package productivityappcac;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisualTimer extends Application {
    private int timeLeft; // seconds
    private Timeline timeline;
    private Label timeLabel;
    private TextField setTimeField;

    private Button startButton, pauseButton, resetButton, setButton, plusButton, minusButton;

    @Override
    public void start(Stage stage) {
        // Main Label
        timeLabel = new Label("00:00");
        timeLabel.setFont(Font.font("Serif", 40));
        timeLabel.setMinWidth(200);
        timeLabel.setAlignment(Pos.CENTER);

        // Timer logic
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (timeLeft > 0) {
                timeLeft--;
                updateLabel();
            } else {
                timeline.stop();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Time's Up!");
                alert.setHeaderText(null);
                alert.setContentText("Time's Up!");
                alert.showAndWait();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Controls
        startButton = new Button("Start");
        pauseButton = new Button("Pause");
        resetButton = new Button("Reset");

        startButton.setOnAction(e -> {
            if (timeLeft > 0 && timeline.getStatus() != Timeline.Status.RUNNING) {
                timeline.play();
            }
        });

        pauseButton.setOnAction(e -> timeline.pause());

        resetButton.setOnAction(e -> {
            timeline.stop();
            timeLeft = 0;
            updateLabel();
        });

        HBox controlBox = new HBox(10, startButton, pauseButton, resetButton);
        controlBox.setAlignment(Pos.CENTER);

        // Set Time Panel
        Label setLabel = new Label("Set (sec):");
        setTimeField = new TextField();
        setTimeField.setPrefWidth(80);
        setButton = new Button("Set");
        HBox setBox = new HBox(10, setLabel, setTimeField, setButton);
        setBox.setAlignment(Pos.CENTER);

        setButton.setOnAction(e -> {
            try {
                int newTime = Integer.parseInt(setTimeField.getText());
                timeLeft = newTime;
                updateLabel();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number!");
                alert.showAndWait();
            }
        });

        // Adjustment Panel
        plusButton = new Button("+1 min");
        minusButton = new Button("-1 min");
        VBox adjustBox = new VBox(10, plusButton, minusButton);
        adjustBox.setAlignment(Pos.CENTER_RIGHT);
        adjustBox.setPadding(new Insets(10));

        plusButton.setOnAction(e -> {
            timeLeft += 60;
            updateLabel();
        });

        minusButton.setOnAction(e -> {
            if (timeLeft >= 60) {
                timeLeft -= 60;
            } else {
                timeLeft = 0;
                timeline.stop();
            }
            updateLabel();
        });

        // Layout
        BorderPane root = new BorderPane();
        root.setCenter(timeLabel);
        root.setBottom(controlBox);
        root.setTop(setBox);
        root.setRight(adjustBox);
        BorderPane.setMargin(controlBox, new Insets(10));
        BorderPane.setMargin(setBox, new Insets(10));

        Scene scene = new Scene(root, 350, 250);
        stage.setTitle("Visual Timer");
        stage.setScene(scene);
        stage.show();
    }

    private void updateLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}