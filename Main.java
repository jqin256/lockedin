package productivityappcac;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        VisualTimer timerApp = new VisualTimer();
        timerApp.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}