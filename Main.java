import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import java.io.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        AppList appList = new AppList();
        File f1 = new File(System.getProperty("user.dir") + "\\data\\checkedapps.txt");
        File f2 = new File(System.getProperty("user.dir") + "\\data\\checkednames.txt");
        File f3 = new File(System.getProperty("user.dir") + "\\data\\checkedpaths.txt");
        if (f1.isFile() && f2.isFile() && f3.isFile()) {
            appList.loadCheckedPaths();
            appList.loadCheckedNames();
            appList.loadCheckedApps();
        }
        appList.retrieveAppPaths();
        appList.retrieveAppNames();
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
        primaryStage.hide();
        primaryStage.initStyle(StageStyle.DECORATED);
        welcomeScreen.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}