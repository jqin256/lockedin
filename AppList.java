import java.util.ArrayList;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.HashMap;
import java.io.*;


public class AppList{
    private ArrayList<String> appNames = new ArrayList<String>();
    private ArrayList<String> appPaths = new ArrayList<String>();
    private TreeSet<String> checkedPaths = new TreeSet<String>();
    private TreeSet<String> checkedNames = new TreeSet<String>();
    private HashMap<String, ArrayList<String>> nameToPath = new HashMap<String, ArrayList<String>>();
    public AppList() {
        
    }

    public void retrieveAppPaths() {
        ArrayList<String> dirs = new ArrayList<String>();
        Process exeFilesProcess;
        Process getDirsProcess;
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                File dir = new File("C:\\");
                String[] getDirs = {"cmd.exe", "/c", "dir", "/b", "/a:d-h"};
                getDirsProcess = Runtime.getRuntime().exec(getDirs, null, dir);
                BufferedReader dirsInput = new BufferedReader(new InputStreamReader(getDirsProcess.getInputStream()));
                String line = dirsInput.readLine();
                while (line != null) {
                    if (!line.equals("Windows") && !line.equals("Drivers")) {
                        dirs.add(line);
                    }
                    line = dirsInput.readLine();
                }

                String[] getExeFiles = { "cmd.exe", "/c", "dir", "/s", "/b", "*.exe", "/a:-d-s", "|", "findstr", "/v", ".exe."};
                for (String s: dirs) {
                    dir = new File("C:\\" + s);
                    exeFilesProcess = Runtime.getRuntime().exec(getExeFiles, null, dir);
                    BufferedReader exeInput = new BufferedReader(new InputStreamReader(exeFilesProcess.getInputStream()));

                    line = exeInput.readLine();
                    while (line != null) {
                        appPaths.add(line);
                        line = exeInput.readLine();
                    }
                    exeFilesProcess.waitFor();
                    exeInput.close();
                }
                
            }
            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void retrieveAppNames() {
        Process itemPropertiesProcess;
        ArrayList<String> pathsForName;
        try {
            for (String s: appPaths) {
                if (!checkedPaths.contains(s)) {
                    String[] getItemProperties = {"powershell", "\n", "Get-ItemProperty", "\'" + s + "\'", "|", "Format-List"};
                    itemPropertiesProcess = Runtime.getRuntime().exec(getItemProperties);
                    BufferedReader propInput = new BufferedReader(new InputStreamReader(itemPropertiesProcess.getInputStream()));
                    String line = propInput.readLine();
                    String appName = "";
                    while (line != null) {
                        if (line.indexOf(":") != -1) appName = line.substring(line.indexOf(":") + 2).trim();
                        if (line.indexOf(":") - line.indexOf("Product") == 7 && !line.trim().equals("Product:")) {
                            checkedNames.add(appName);
                            if (nameToPath.containsKey(appName)) pathsForName = nameToPath.get(appName);
                            else pathsForName = new ArrayList<String>();
                            pathsForName.add(s);
                            nameToPath.put(appName, pathsForName);
                        }
                        line = propInput.readLine();
                    }
                    checkedPaths.add(s);
                    itemPropertiesProcess.waitFor();
                    propInput.close();
                }
            }
        }
        catch (IOException | InterruptedException e) {
             e.printStackTrace();
        }
    }

    public void saveCheckedNames() {
        try {
            FileWriter fWriter = new FileWriter(System.getProperty("user.dir") + "\\data\\checkednames.txt", false);
            Spliterator<String> it = checkedNames.spliterator();
            while (it.tryAdvance(name -> {
                try {
                    fWriter.write(name + "\n");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            fWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCheckedPaths() {
        try {
            FileWriter fWriter = new FileWriter(System.getProperty("user.dir") + "\\data\\checkedpaths.txt", false);
            Spliterator<String> it = checkedPaths.spliterator();
            while (it.tryAdvance(path -> {
                try {
                    fWriter.write(path + "\n");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            fWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCheckedApps() {
        try {
            FileWriter fWriter = new FileWriter(System.getProperty("user.dir") + "\\data\\checkedapps.txt", false);
            Spliterator<String> it = checkedNames.spliterator();
            while (it.tryAdvance(name -> {
                try {
                    ArrayList<String> pathsForName = nameToPath.get(name);
                    for (String s: pathsForName) {
                        fWriter.write(s + "|" + name + "\n");
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            fWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCheckedApps() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data\\checkedapps.txt"));
            String line = input.readLine();
            ArrayList<String> pathsForName;
            while (line != null) {
                line = line.trim();
                String name = line.substring(line.indexOf("|") + 1);
                String path = line.substring(0, line.indexOf("|"));
                if (nameToPath.containsKey(name)) pathsForName = nameToPath.get(name);
                else pathsForName = new ArrayList<String>();
                pathsForName.add(path);
                nameToPath.put(name, pathsForName);
                line = input.readLine();
            }
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCheckedNames() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data\\checkednames.txt"));
            String line = input.readLine();
            while (line != null) {
                line = line.trim();
                checkedNames.add(line);
                line = input.readLine();
            }
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCheckedPaths() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\data\\checkedpaths.txt"));
            String line = input.readLine();
            while (line != null) {
                line = line.trim();
                appPaths.add(line);
                checkedPaths.add(line);
                line = input.readLine();
            }
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAppPaths() {
        return appPaths;
    }
    public ArrayList<String> getAppNames() {
        Spliterator<String> it = checkedNames.spliterator();
        while (it.tryAdvance(name -> {
            appNames.add(name);
        }));
        return appNames;
    }
    public HashMap<String, ArrayList<String>> getNameToPath() {
        return nameToPath;
    }
}