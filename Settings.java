import javafx.application.*;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.*;
import javafx.collections.*;
import java.util.*;
import javafx.animation.*;
import javafx.util.*;

public class Settings extends Application {
    @FXML
    private Button general, lockedApps;
    private Text themeText, lockedAppsExplanation;
    private MenuButton themeMenuButton;
    private String theme;
    private LockList selectedLockList = new LockList();
    
    public void start(Stage stage) {
        themeText = new Text("Background Theme");
        MenuItem lightTheme = new MenuItem("Light");
        theme = "Light";
        MenuItem darkTheme = new MenuItem("Dark");

        MenuItem[] themeMenu = {lightTheme, darkTheme};
        themeMenuButton = new MenuButton(theme, new Rectangle(), themeMenu);
        themeMenuButton.setOnAction(e -> {
            themeMenuButton.show();
        });
        
        lightTheme.setOnAction(e -> {
            themeMenuButton.setText("Light");
            theme = "Light";
        });
        darkTheme.setOnAction(e -> {
            themeMenuButton.setText("Dark");
            theme = "Dark";
        });

        VBox genSettings = new VBox(4, themeText, themeMenuButton);

        Button genButton = new Button("General");
        Button appsButton = new Button("Locked Apps");

        VBox categories = new VBox(4, genButton, appsButton);

        AnchorPane.setLeftAnchor(categories, 10.0);
        AnchorPane.setLeftAnchor(genSettings, 360.0);
        Scene genScene = new Scene(new AnchorPane(categories, genSettings), 1080, 720);
        
        stage.setTitle("Settings");
        stage.setScene(genScene);
        stage.show();

        //move app processing to main file
        ListView<String> apps = new ListView<String>();
        apps.setPrefWidth(700);
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

        apps.setItems(appNames);
        VBox appSettings = new VBox(apps);
        apps.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        apps.setOnMouseClicked(e -> {
            ObservableList<String> selectedItems =  apps.getSelectionModel().getSelectedItems();
            ArrayList<String> selectedApps = new ArrayList<String>();
            for (String s: selectedItems) {
                s = s + " ";
                while (s.indexOf(" ") != -1) {
                    selectedApps.add(s.substring(0, s.indexOf(" ")));
                    s = s.substring(s.indexOf(" ") + 1);
                }
            }
            selectedLockList.setLockList(selectedApps);
            //System.out.println(selectedLockList.getLockList());
        });
        
        genButton.setOnAction(e -> {
            stage.getScene().setRoot(new AnchorPane(categories, genSettings));
        });
        appsButton.setOnAction(e -> {
            AnchorPane root = new AnchorPane(categories, appSettings);
            AnchorPane.setLeftAnchor(appSettings, 360.0);
            stage.getScene().setRoot(root);

            Timeline killProcessLoop = new Timeline(new KeyFrame(Duration.seconds(1), d-> {
                selectedLockList.killProcesses();
            }));
            killProcessLoop.setCycleCount(Timeline.INDEFINITE);
            killProcessLoop.play();
        });
    }

    public LockList getSelectedLockList() {
        return selectedLockList;
    }
}