
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        AppList appList = new AppList();
        appList.retrieveAppNames();
        appList.retrieveAppPaths();
        File f1 = new File("checkedapps.txt");
        File f2 = new File("checkednames.txt");
        File f3 = new File("checkedpaths.txt");
        if (f1.isFile() && f2.isFile() && f3.isFile()) {
            appList.loadCheckedPaths();
            appList.loadCheckedNames();
            appList.loadCheckedApps();
        }
        appList.saveCheckedPaths();
        appList.saveCheckedNames();
        appList.saveCheckedApps();
        
        ArrayList<String> appArrayList = appList.getAppNames();
        ObservableList<String> appNames = FXCollections.observableArrayList(appArrayList);

        Settings settingsPage = new Settings();
        settingsPage.setAppNames(appNames);
        settingsPage.setNameToPath(appList.getNameToPath());
        
        VisualTimer timerApp = new VisualTimer();
        timerApp.setNameToPath(appList.getNameToPath());

        WelcomeScreen welcomeScreen = new WelcomeScreen(timerApp, settingsPage);
        settingsPage.setWelcomeScreen(welcomeScreen);
        timerApp.setWelcomeScreen(welcomeScreen);
        BackgroundMusic.playMusic();
        welcomeScreen.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}