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
import java.io.*;
import javafx.collections.transformation.*;

public class Settings extends Application {
    private WelcomeScreen welcomeScreen;
    public Settings(WelcomeScreen w) {
        welcomeScreen = w;
    }
    public Settings() {

    }
    @FXML
    private Button general, lockedApps;
    private Text themeText, lockedAppsExplanation;
    private MenuButton themeMenuButton;
    private String theme;
    private LockList selectedLockList = new LockList();
    private FilteredList<String> appNames;
    private VBox categories;
    private VBox genSettings;
    private ArrayList<String> selectedApps;
    private ListView<String> apps = new ListView<String>();
    private ListView<String> selectedAppView = new ListView<String>();
    private HashMap<String, String> nameToPath;
    private String[] banList = {".", "Update", "Setup", "Install", "Driver", "Service", "Utility", "Uninstall", "?"};
    private TreeSet<String> selectedAppNames;
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
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> {
            welcomeScreen.start(stage);
        });
        categories = new VBox(4, homeButton, genButton, appsButton);
        AnchorPane.setTopAnchor(categories, 20.0);
        AnchorPane.setLeftAnchor(categories, 10.0);
        AnchorPane.setLeftAnchor(genSettings, 360.0);
        
        Scene genScene = new Scene(new AnchorPane(categories, genSettings), 1080, 720);
        
        stage.setTitle("Settings");
        stage.setScene(genScene);
        stage.show();

//Locked apps code
        apps.setPrefWidth(700);
        apps.setItems(appNames);
        lockedAppsExplanation = new Text("Select the apps you would like to lock below. Hold Ctrl while clicking to select multiple apps.");

        apps.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedApps = new ArrayList<String>();
        selectedAppNames = new TreeSet<String>();
        selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here:");
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            appNames.setPredicate(s -> s.toLowerCase().contains(newValue.toLowerCase().trim()));
        });
        try {
            File f = new File("locklist.txt");
            if (f.isFile()) {
                BufferedReader br = new BufferedReader(new FileReader("locklist.txt"));
                String line = br.readLine();
                while (line != null) {
                    line = line.strip();
                    apps.getSelectionModel().select(line);
                    selectedAppNames.add(line);
                    selectedApps.add(nameToPath.get(line));
                    line = br.readLine();
                }
                selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
                br.close();
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        selectedLockList.setLockList(selectedApps);

        apps.setOnMouseClicked(e -> {
            ObservableList<String> selectedItems =  apps.getSelectionModel().getSelectedItems();
            selectedApps = new ArrayList<String>();
            try {
                FileWriter fWriter = new FileWriter("locklist.txt", false);
                for (String s: selectedItems) {
                    selectedAppNames.add(s);
                }
                Spliterator<String> it = selectedAppNames.spliterator();
                while (it.tryAdvance(name -> {
                    try {
                        fWriter.write(name + "\n");
                        selectedApps.add(nameToPath.get(name));
                    }
                    catch (IOException error) {
                        System.err.println(error.getMessage());
                    }
                }));
                fWriter.close();
            }
            catch (IOException error) {
                System.err.println(error.getMessage());
            }
            selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
            selectedLockList.setLockList(selectedApps);
        });

        selectedAppView.setOnMouseClicked(e -> {
            ObservableList<String> selectedItems =  selectedAppView.getSelectionModel().getSelectedItems();
            for (String s: selectedItems) {
                selectedAppNames.remove(s);
                selectedApps.remove(nameToPath.get(s));
            }
            selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
            selectedLockList.setLockList(selectedApps);
            try {
                FileWriter fWriter = new FileWriter("locklist.txt", false);
                Spliterator<String> it = selectedAppNames.spliterator();
                while (it.tryAdvance(name -> {
                    try {
                        fWriter.write(name + "\n");
                        selectedApps.add(nameToPath.get(name));
                    }
                    catch (IOException error) {
                        System.err.println(error.getMessage());
                    }
                }));
                fWriter.close();
            }
            catch (IOException error) {
                System.err.println(error.getMessage());
            }
        });

        VBox appSettings = new VBox(4.0, lockedAppsExplanation, apps, searchBar, selectedAppView);

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
        int i = 0;
        boolean removed = false;
        while (i < a.size()) {
            removed = false;
            for (int j = 0; j < banList.length; ++j) {
                if (a.get(i).indexOf(banList[j]) != -1) {
                    a.remove(i);
                    removed = true;
                }
            }
            if (!removed) i++;
        }
        appNames = new FilteredList<String>(a);
    }

    public void setNameToPath(HashMap<String, String> a) {
        nameToPath = a;
    }

    public LockList getSelectedLockList() {
        return selectedLockList;
    }
    public void setWelcomeScreen(WelcomeScreen w) {
        welcomeScreen = w;
    }
}