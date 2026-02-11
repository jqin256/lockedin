<<<<<<< Updated upstream
=======

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
>>>>>>> Stashed changes
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
    private HashMap<String, String> nameToPath = new HashMap<String, String>();
    public AppList() {
        
    }
    //use dir /s /b *.exe /a:-d | findstr /v .exe. to find all executable files
    //if app has blank product skip
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
        try {
            for (String s: appPaths) {
                if (!checkedPaths.contains(s)) {
                    String[] getItemProperties = {"powershell", "\n", "Get-ItemProperty", "\'" + s + "\'", "|", "Format-List"};
                    itemPropertiesProcess = Runtime.getRuntime().exec(getItemProperties);
                    BufferedReader propInput = new BufferedReader(new InputStreamReader(itemPropertiesProcess.getInputStream()));
                    String line = propInput.readLine();
                    String appName;
                    while (line != null) {
                        appName = line.substring(line.indexOf(":") + 2).strip();
                        if (line.indexOf(":") - line.indexOf("Product") == 7 && !line.strip().equals("Product:") && !checkedNames.contains(appName)) {
                            appNames.add(appName);
                            checkedNames.add(appName);
                            nameToPath.put(appName, s);
                        }
                        checkedPaths.add(s);
                        line = propInput.readLine();
                    }
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
            FileWriter fWriter = new FileWriter("checkednames.txt", false);
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
            FileWriter fWriter = new FileWriter("checkedpaths.txt", false);
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
    //need checkedapps to define nameToPath to link checkednames.txt and checkedpaths.txt information
    public void saveCheckedApps() {
        try {
            FileWriter fWriter = new FileWriter("checkedapps.txt", false);
            Spliterator<String> it = checkedNames.spliterator();
            while (it.tryAdvance(name -> {
                try {
                    fWriter.write(nameToPath.get(name) + "|" + name + "\n");
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
            BufferedReader input = new BufferedReader(new FileReader("checkedapps.txt"));
            String line = input.readLine();
            while (line != null) {
                line = line.strip();
                nameToPath.put(line.substring(line.indexOf("|") + 1), line.substring(0, line.indexOf("|")));
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
            BufferedReader input = new BufferedReader(new FileReader("checkednames.txt"));
            String line = input.readLine();
            while (line != null) {
                line = line.strip();
                appNames.add(line);
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
            BufferedReader input = new BufferedReader(new FileReader("checkedpaths.txt"));
            String line = input.readLine();
            while (line != null) {
                line = line.strip();
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
        return appNames;
    }
    public HashMap<String, String> getNameToPath() {
        return nameToPath;
    }
}