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
    private ObservableList<String> appNames;
    private VBox categories;
    private VBox genSettings;
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

        genSettings = new VBox(4, themeText, themeMenuButton);

        Button genButton = new Button("General");
        Button appsButton = new Button("Locked Apps");

        categories = new VBox(4, genButton, appsButton);

        AnchorPane.setLeftAnchor(categories, 10.0);
        AnchorPane.setLeftAnchor(genSettings, 360.0);
        Scene genScene = new Scene(new AnchorPane(categories, genSettings), 1080, 720);
        
        stage.setTitle("Settings");
        stage.setScene(genScene);
        stage.show();

        //Locked apps code
        //move app processing to main file
        ListView<String> apps = new ListView<String>();
        apps.setPrefWidth(700);
        apps.setItems(appNames);
        lockedAppsExplanation = new Text("Select the apps you would like to lock below. Hold Ctrl while clicking to select multiple apps.");
        VBox appSettings = new VBox(lockedAppsExplanation, apps);
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

    public void setAppNames(ObservableList<String> a) {
        appNames = a;
    }

    public LockList getSelectedLockList() {
        return selectedLockList;
    }

    public AnchorPane genRoot() {
        return (new AnchorPane(categories, genSettings));
    }
}