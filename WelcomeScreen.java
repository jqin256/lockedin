import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeScreen extends Application {

    private Settings settingsPage;
    private VisualTimer timer;

    public WelcomeScreen(VisualTimer v, Settings s) {
        timer = v;
        settingsPage = s;
    }

    public WelcomeScreen() {}

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LockedIn - Welcome");

        /* ================= BACKGROUND ================= */

        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#2193b0")),
                new Stop(1, Color.web("#6dd5ed"))
        };

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(
                new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        /* ================= TITLE ================= */

        Label title = new Label("LockedIn");
        title.setFont(Font.font("Segoe UI Semibold", 36));
        title.setTextFill(Color.WHITE);

        DropShadow glow = new DropShadow(30, Color.web("#ffffff80"));
        title.setEffect(glow);

        VBox titleBox = new VBox(5,
                title,
                subtitle("Focus deeper. Achieve more.")
        );
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(35, 0, 15, 0));

        root.setTop(titleBox);

        /* ================= BUTTONS ================= */

        Button timerButton = createStyledButton("My Timer", "#2ecc71", "#58d68d");
        Button settingsButton = createStyledButton("Settings", "#3498db", "#5dade2");

        VBox buttonBox = new VBox(22, timerButton, settingsButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30));
        buttonBox.setOpacity(0);

        root.setCenter(buttonBox);

        /* ================= FOOTER ================= */

        Label footer = new Label("Stay productive. Stay LockedIn.");
        footer.setFont(Font.font("Segoe UI", 13));
        footer.setTextFill(Color.WHITE);
        footer.setOpacity(0.85);
        BorderPane.setAlignment(footer, Pos.CENTER);
        root.setBottom(footer);

        /* ================= SCENE ================= */

        Scene scene = new Scene(root, 460, 380);
        primaryStage.setScene(scene);
        primaryStage.show();

        /* ================= ANIMATIONS ================= */

        playIntroAnimations(titleBox, buttonBox);

        /* ================= ACTIONS ================= */

        timerButton.setOnAction(e -> timer.start(primaryStage));
        settingsButton.setOnAction(e -> settingsPage.start(primaryStage));
    }

    /* =================================================
       Helper Components
       ================================================= */

    private Label subtitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", 14));
        label.setTextFill(Color.web("#e8f6ff"));
        return label;
    }

    private Button createStyledButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", 16));
        button.setTextFill(Color.WHITE);
        button.setPrefWidth(230);
        button.setPadding(new Insets(12, 20, 12, 20));

        button.setBackground(new Background(new BackgroundFill(
                Color.web(baseColor), new CornerRadii(14), Insets.EMPTY)));

        DropShadow shadow = new DropShadow(15, Color.rgb(0, 0, 0, 0.3));
        button.setEffect(shadow);

        /* Hover color animation */
        button.setOnMouseEntered(e -> animateColor(button, baseColor, hoverColor));
        button.setOnMouseExited(e -> animateColor(button, hoverColor, baseColor));

        /* Click “pop” animation */
        button.setOnMousePressed(e -> scale(button, 0.96));
        button.setOnMouseReleased(e -> scale(button, 1.0));

        return button;
    }

    private void animateColor(Button button, String from, String to) {
        Color start = Color.web(from);
        Color end = Color.web(to);
        ObjectProperty<Color> color = new SimpleObjectProperty<>(start);

        color.addListener((obs, o, n) ->
                button.setBackground(new Background(
                        new BackgroundFill(n, new CornerRadii(14), Insets.EMPTY)))
        );

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, start)),
                new KeyFrame(Duration.millis(250), new KeyValue(color, end))
        );
        timeline.play();
    }

    private void scale(Button button, double value) {
        ScaleTransition st = new ScaleTransition(Duration.millis(120), button);
        st.setToX(value);
        st.setToY(value);
        st.play();
    }

    private void playIntroAnimations(VBox titleBox, VBox buttons) {

        TranslateTransition titleSlide = new TranslateTransition(
                Duration.millis(900), titleBox);
        titleSlide.setFromY(-40);
        titleSlide.setToY(0);
        titleSlide.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition titleFade = new FadeTransition(
                Duration.millis(900), titleBox);
        titleFade.setFromValue(0);
        titleFade.setToValue(1);

        FadeTransition buttonFade = new FadeTransition(
                Duration.millis(800), buttons);
        buttonFade.setFromValue(0);
        buttonFade.setToValue(1);
        buttonFade.setDelay(Duration.millis(500));

        TranslateTransition buttonBounce = new TranslateTransition(
                Duration.millis(800), buttons);
        buttonBounce.setFromY(25);
        buttonBounce.setToY(0);
        buttonBounce.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(
                titleSlide, titleFade,
                buttonFade, buttonBounce
        ).play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
