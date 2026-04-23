import javafx.application.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.*;
import javafx.collections.*;
import java.util.*;
import javafx.util.*;
import java.io.*;
import javafx.collections.transformation.*;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Settings extends Application {
    private WelcomeScreen welcomeScreen;
    public Settings(WelcomeScreen w) {
        welcomeScreen = w;
    }
    public Settings() {

    }
    @FXML
    private Text themeText, lockedAppsExplanation;
    private MenuButton themeMenuButton;
    private String theme;
    private LockList selectedLockList = new LockList();
    private FilteredList<String> appNames;
    private VBox categories, genSettings;
    private HashSet<String> selectedAppSet = new HashSet<String>();
    private ListView<String> apps = new ListView<String>();
    private ListView<String> selectedAppView = new ListView<String>();
    private HashMap<String, ArrayList<String>> nameToPath;
    private String[] banList = {".", "Update", "Setup", "Install", "Driver", "Service", "Utility", "Uninstall", "?", "Extension", "Updater"};
    private TreeSet<String> selectedAppNames;
    private ListView<String> playlistView = new ListView<String>();
    private final Color playlistBoxBaseColor = Color.web("#202020");
    public void start(Stage stage) {
        //used for all subheader text within each settings category
        Font textFont = Font.font("Segoe UI Semibold", 18);

        themeText = new Text("Background Theme");
        themeText.setFont(textFont);
        themeText.setFill(Color.WHITE);
        MenuItem lightTheme = new MenuItem("Light");
        theme = "Light";
        MenuItem darkTheme = new MenuItem("Dark");

        MenuItem[] themeMenu = {lightTheme, darkTheme};
        themeMenuButton = settingsStyledMenuButton(theme, "#1b7b93", themeMenu);
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

        Button genButton = settingsStyledButton("General", "#2193b0");
        Button appsButton = settingsStyledButton("Locked Apps", "#2193b0");
        Button musicButton = settingsStyledButton("Music", "#2193b0");
        Button homeButton = settingsStyledButton("Home", "#2193b0");

        homeButton.setOnAction(e -> {
            welcomeScreen.start(stage);
        });

//Locked apps code
        apps.setPrefWidth(480);
        apps.setPrefHeight(250);
        apps.setItems(appNames);
        apps.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override public ListCell<String> call(ListView<String> lsV) {
                return new ColorfulCell(playlistBoxBaseColor);
            }
        });
        apps.setBackground(new Background(
            new BackgroundFill(playlistBoxBaseColor.brighter(), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        lockedAppsExplanation = new Text("Select the apps you would like to lock below. Hold Ctrl while clicking to select multiple apps.");
        lockedAppsExplanation.setFont(textFont);
        lockedAppsExplanation.setFill(Color.WHITE);
        apps.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedAppNames = new TreeSet<String>();
        selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
        selectedAppView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override public ListCell<String> call(ListView<String> lsV) {
                return new ColorfulCell(playlistBoxBaseColor);
            }
        });
        selectedAppView.setBackground(new Background(
            new BackgroundFill(playlistBoxBaseColor.brighter(), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        selectedAppView.setPrefHeight(250);
        
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here:");
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            appNames.setPredicate(s -> s.toLowerCase().contains(newValue.toLowerCase().trim()));
        });
        searchBar.setStyle("-fx-text-fill: #ffffff");
        searchBar.setBackground(new Background(
            new BackgroundFill(playlistBoxBaseColor.brighter().brighter(), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        try {
            File f = new File(System.getProperty("user.dir") + "\\data\\locklist.txt");
            if (f.isFile()) {
                BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data\\locklist.txt"));
                String line = br.readLine();
                while (line != null) {
                    line = line.trim();
                    apps.getSelectionModel().select(line);
                    selectedAppNames.add(line);
                    ArrayList<String> pathsForName = nameToPath.get(line);
                    for (String s: pathsForName) {
                        selectedAppSet.add(s);
                    }
                    line = br.readLine();
                }
                selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
                br.close();
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        selectedLockList.setLockList(selectedAppSet);

        apps.setOnMouseClicked(e -> {
            ObservableList<String> selectedItems =  apps.getSelectionModel().getSelectedItems();
            try {
                FileWriter fWriter = new FileWriter(System.getProperty("user.dir") + "\\data\\locklist.txt", false);
                for (String s: selectedItems) {
                    selectedAppNames.add(s);
                }
                Spliterator<String> it = selectedAppNames.spliterator();
                while (it.tryAdvance(name -> {
                    try {
                        fWriter.write(name + "\n");
                        ArrayList<String> pathsForName = nameToPath.get(name);
                        for (String s: pathsForName) {
                            selectedAppSet.add(s);
                        }
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
            selectedLockList.setLockList(selectedAppSet);
        });

        selectedAppView.setOnMouseClicked(e -> {
            ObservableList<String> selectedItems =  selectedAppView.getSelectionModel().getSelectedItems();
            for (String s: selectedItems) {
                selectedAppNames.remove(s);
                ArrayList<String> pathsForName = nameToPath.get(s);
                for (String s2: pathsForName) {
                    selectedAppSet.remove(s2);
                }
            }
            selectedAppView.setItems(FXCollections.observableArrayList(selectedAppNames));
            selectedLockList.setLockList(selectedAppSet);
            try {
                FileWriter fWriter = new FileWriter(System.getProperty("user.dir") + "\\data\\locklist.txt", false);
                Spliterator<String> it = selectedAppNames.spliterator();
                while (it.tryAdvance(name -> {
                    try {
                        fWriter.write(name + "\n");
                        ArrayList<String> pathsForName = nameToPath.get(name);
                        for (String s: pathsForName) {
                            selectedAppSet.add(s);
                        }
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
        Text selectedAppExplanation = new Text("Here are the apps you selected to be locked while locked in. You can click to remove them.");
        selectedAppExplanation.setFont(textFont);
        selectedAppExplanation.setFill(Color.WHITE);

        VBox appSettings = new VBox(4.0, lockedAppsExplanation, apps, searchBar, selectedAppExplanation, selectedAppView);

        //add music / create playlists
        ObservableList<String> playlists = FXCollections.observableArrayList();
        Text playlistNamePrompt = new Text("Enter playlist name below:");
        playlistNamePrompt.setFont(textFont);
        playlistNamePrompt.setFill(Color.WHITE);
        TextField namePlaylist = new TextField("");
        namePlaylist.setStyle("-fx-text-fill: #ffffff");
        namePlaylist.setBackground(new Background(
            new BackgroundFill(playlistBoxBaseColor.brighter().brighter(), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        Button createPlaylist = settingsStyledButton("Create Playlist", "#1b7b93");
        createPlaylist.setPadding(new Insets(4, 10, 4, 10));
        HBox playlistCreation = new HBox(4.0, namePlaylist, createPlaylist);

        File playlistPath = new File(System.getProperty("user.dir") + "\\music");
        String[] directories = playlistPath.list(new FilenameFilter() {
            @Override public boolean accept(File curr, String name) {
                return new File(curr, name).isDirectory();
            }
        });
        for (String s: directories) {
            while (s.indexOf("\\") >= 0) {
                s = s.substring(s.indexOf("\\") + 1);
            }
            playlists.add(s);
        }
        playlistView.setItems(playlists);

        playlistView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override public ListCell<String> call(ListView<String> lsV) {
                return new ColorfulCell(playlistBoxBaseColor);
            }
        });
        playlistView.setBackground(new Background(
            new BackgroundFill(playlistBoxBaseColor.brighter(), CornerRadii.EMPTY, Insets.EMPTY)
        ));

        createPlaylist.setOnAction(e -> {
            if (namePlaylist.getText() != null || namePlaylist.getText() != "") {
                playlists.add(namePlaylist.getText());
                playlistView.setItems(playlists);
                File playlistDirectory = new File(playlistPath.getAbsolutePath() + "\\" + namePlaylist.getText().trim());
                namePlaylist.setText("");
                if (!playlistDirectory.exists()) {
                    playlistDirectory.mkdir();
                }
            }
        });
        Button addToPlaylist = settingsStyledButton("Add to Playlist", "#1b7b93");
        addToPlaylist.setVisible(false);
        addToPlaylist.setMaxWidth(50);
        playlistView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addToPlaylist.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Browse...");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav", "*.mp3"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home").toString() + "\\Music"));
            List<File> selectedMedia = fileChooser.showOpenMultipleDialog(stage);
            if (selectedMedia != null) {
                for (File f: selectedMedia) {
                    for (String s: playlistView.getSelectionModel().getSelectedItems()) {
                        String pathString = f.toString();
                        while (pathString.indexOf("\\") >= 0) {
                            pathString = pathString.substring(pathString.indexOf("\\") + 1);
                        }
                        File f2 = new File("music\\" + s + "\\" + pathString);
                        try {
                            f2.createNewFile();
                        }
                        catch(IOException error) {
                            System.err.println(error.getMessage());
                        }
                    }
                }
            }
        });

        Button deletePlaylist = settingsStyledButton("Delete", "#1b7b93");
        deletePlaylist.setOnMouseClicked(e -> {

        });
        
        playlistView.setOnMouseClicked(e -> {
            addToPlaylist.setVisible(true);
        });


        VBox musicSettings = new VBox(4.0, playlistNamePrompt, playlistCreation, playlistView, addToPlaylist);

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Rectangle categoryBackground = new Rectangle();
        categoryBackground.setWidth(220);
        categoryBackground.setHeight(screenBounds.getHeight());
        categoryBackground.setFill(Color.web("#2193b0").darker().darker());
        categories = new VBox(homeButton, genButton, appsButton, musicButton);

        genButton.setOnAction(e -> {
            AnchorPane r = new AnchorPane(categories, categoryBackground, genSettings);
            categoryBackground.toBack();
            AnchorPane.setLeftAnchor(genSettings, 360.0);
            setRootGUI(r, categories, genSettings);
            stage.getScene().setRoot(r);
        });
        appsButton.setOnAction(e -> {
            AnchorPane r = new AnchorPane(categories, categoryBackground, appSettings);
            categoryBackground.toBack();
            AnchorPane.setLeftAnchor(appSettings, 360.0);
            setRootGUI(r, categories, appSettings);
            stage.getScene().setRoot(r);
        });
        musicButton.setOnAction(e -> {
            AnchorPane r = new AnchorPane(categories, categoryBackground, musicSettings);
            categoryBackground.toBack();
            AnchorPane.setLeftAnchor(musicSettings, 360.0);
            setRootGUI(r, categories, musicSettings);
            stage.getScene().setRoot(r);
        });

        AnchorPane root = new AnchorPane(categories, categoryBackground, genSettings);
        categoryBackground.toBack();
        setRootGUI(root, categories, genSettings);
        Scene genScene = new Scene(root, 1080, 720);
        
        stage.setTitle("Settings");
        stage.setScene(genScene);
        stage.show();
    }

    public void setAppNames(ObservableList<String> a) {
        int i = 0;
        boolean removed = false;
        while (i < a.size()) {
            removed = false;
            for (int j = 0; j < banList.length; ++j) {
                if (a.get(i).indexOf(banList[j]) != -1 && a.size() != 0) {
                    a.remove(i);
                    removed = true;
                }
            }
            if (!removed) i++;
        }
        appNames = new FilteredList<String>(a);
    }

    public void setNameToPath(HashMap<String, ArrayList<String>> a) {
        nameToPath = a;
    }

    public LockList getSelectedLockList() {
        return selectedLockList;
    }
    public void setWelcomeScreen(WelcomeScreen w) {
        welcomeScreen = w;
    }
    private void setRootGUI(AnchorPane r, VBox lcol, VBox rcol) {
        AnchorPane.setTopAnchor(rcol, 20.0);
        AnchorPane.setLeftAnchor(rcol, 240.0);
        r.setBackground(new Background(
                new BackgroundFill(Color.web("#2193b0"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private Button settingsStyledButton(String t, String c) {
        Button b = new Button(t);
        b.setFont(Font.font("Segoe UI", 14));
        b.setTextFill(Color.WHITE);
        int defaultTopInset = 8;
        int defaultBottomInset = 8;
        int defaultRightInset = 18;
        int defaultLeftInset = 18;
        b.setPadding(new Insets(defaultTopInset, defaultRightInset, defaultBottomInset, defaultLeftInset));
        b.setMinWidth(220);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setShape(new Rectangle(20, 220));

        b.setBackground(new Background(new BackgroundFill(
                Color.web(c).darker(), new CornerRadii(0), Insets.EMPTY)));

        b.setOnMouseEntered(e -> {
            b.setBackground(new Background(new BackgroundFill(
                Color.web(c).darker().darker(), new CornerRadii(0), Insets.EMPTY)));
        });
        b.setOnMouseExited(e -> {
            b.setBackground(new Background(new BackgroundFill(
                Color.web(c).darker(), new CornerRadii(0), Insets.EMPTY)));
        });

        return b;
    }

    private MenuButton settingsStyledMenuButton(String s, String c, MenuItem[] m) {
        MenuButton b = new MenuButton(s, new Rectangle(), m);

        b.setFont(Font.font("Segoe UI", 14));
        b.setTextFill(Color.WHITE);

        b.setBackground(new Background(new BackgroundFill(
                Color.web(c), new CornerRadii(14), Insets.EMPTY)));

        b.setOnMouseEntered(e -> {
            b.setBackground(new Background(new BackgroundFill(
                Color.web(c).darker(), new CornerRadii(14), Insets.EMPTY)));
        });
        b.setOnMouseExited(e -> {
            b.setBackground(new Background(new BackgroundFill(
                Color.web(c), new CornerRadii(14), Insets.EMPTY)));
        });
        
        return b;
    }
}