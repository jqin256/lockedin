import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeScreen extends Application {
    private Settings settingsPage;
    private VisualTimer timer;
    public WelcomeScreen (VisualTimer v, Settings s)
    {
        timer = v;
        settingsPage = s;
    }
    
    public WelcomeScreen() {

    }
    @Override   
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LockedIn - Welcome");

        // Root layout with gradient background
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#2193b0")),  // ocean blue
            new Stop(1, Color.web("#6dd5ed"))   // light turquoise
        };
        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(backgroundGradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Title
        Label titleLabel = new Label("Welcome to LockedIn");
        titleLabel.setFont(Font.font("Segoe UI Semibold", 30));
        titleLabel.setTextFill(Color.WHITE);
        BorderPane.setMargin(titleLabel, new Insets(40, 0, 10, 0));
        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        // Buttons
        Button timerButton = createStyledButton("My Timer", "#2ecc71", "#58d68d"); // green
        Button settingsButton = createStyledButton("Settings", "#3498db", "#5dade2");         // blue

        VBox buttonBox = new VBox(20, timerButton, settingsButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 60, 30, 60));
        root.setCenter(buttonBox);

        // Footer
        Label footer = new Label("Stay productive. Stay LockedIn.");
        footer.setFont(Font.font("Segoe UI", 13));
        footer.setTextFill(Color.WHITE);
        BorderPane.setAlignment(footer, Pos.CENTER);
        footer.setPadding(new Insets(10));
        root.setBottom(footer);

        // Scene
        Scene scene = new Scene(root, 440, 360);
        primaryStage.setScene(scene);

        // Fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Button actions (temporary placeholders)
        timerButton.setOnAction(e -> primaryStage.getScene().setRoot(timer.getRoot()));
        settingsButton.setOnAction(e -> primaryStage.getScene().setRoot(settingsPage.genRoot()));

        primaryStage.show();
    }

    /**
     * Creates a stylized button with color and hover animation.
     */
    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", 16));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(Color.web(baseColor), new CornerRadii(12), Insets.EMPTY)));
        button.setPadding(new Insets(10, 20, 10, 20));
        button.setPrefWidth(220);
        button.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.25)));

        // Hover animation (smooth color transition)
        button.setOnMouseEntered(e -> animateColor(button, baseColor, hoverColor));
        button.setOnMouseExited(e -> animateColor(button, hoverColor, baseColor));

        return button;
    }

    /**
     * Smoothly interpolates between two colors over time.
     */
    private void animateColor(Button button, String fromColor, String toColor) {
        Color start = Color.web(fromColor);
        Color end = Color.web(toColor);
        final ObjectProperty<Color> color = new SimpleObjectProperty<>(start);

        color.addListener((obs, oldVal, newVal) -> {
            button.setBackground(new Background(new BackgroundFill(newVal, new CornerRadii(12), Insets.EMPTY)));
        });

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(color, start)),
            new KeyFrame(Duration.millis(250), new KeyValue(color, end))
        );
        timeline.play();
    }

    /**
     * Temporary message popup (you can replace with navigation logic later).
     */
    private void showMessage(String msg) {
        Stage popup = new Stage();
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label label = new Label(msg);
        label.setFont(Font.font("Segoe UI", 14));
        label.setTextFill(Color.web("#333"));

        box.getChildren().add(label);
        Scene scene = new Scene(box, 260, 100);
        popup.setScene(scene);
        popup.setTitle("LockedIn");
        popup.show();

        // Auto-close popup
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(e -> popup.close());
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
