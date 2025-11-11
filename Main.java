import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ObservableList<String> appNames = FXCollections.observableArrayList();
        AppList appList = new AppList();
        appList.retrieveAppList();
        ArrayList<String> appArrayList = appList.getInstalledApps();
        boolean preApps = true;
        while (preApps) {
            if (appArrayList.remove(0).indexOf("--") > -1) {
                preApps = false;
            }
        }

        for (String s: appArrayList) {
            App a = new App();
            a.outputToApp(s);
            appNames.add(a.getName());
        }

        ArrayList<String> protectedNames = new ArrayList<String>();
        protectedNames.add("Microsoft");
        protectedNames.add("Windows");
        protectedNames.add("Extension");
        protectedNames.add("Intel");
        protectedNames.add("Dell");
        protectedNames.add("Lenovo");
        protectedNames.add("HP");

        int i;
        for (String s1: protectedNames) {
            i = 0;
            while (i < appNames.size()) {
                if (appNames.get(i).indexOf(s1) != -1) {
                    appNames.remove(i);
                }
                else {
                    i++;
                }
            }
        }

        Settings settingsPage = new Settings();
        settingsPage.setAppNames(appNames);
        VisualTimer timerApp = new VisualTimer();
        WelcomeScreen welcomeScreen = new WelcomeScreen(timerApp, settingsPage);

        
        welcomeScreen.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}