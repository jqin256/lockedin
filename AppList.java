import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AppList {
    private ArrayList<String> apps;
    public AppList () {
        
    }

    public void setAppList() {
        ArrayList<String> result = new ArrayList<String>();
        Process p;
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                String[] cmd = {"winget", "ls"};
                p = Runtime.getRuntime().exec(cmd);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = input.readLine()) != null) {
                    result.add(line);
                }
                p.waitFor();
            }
            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            apps = result;
        }
    }

    public ArrayList<String> getInstalledApps() {
        return apps;
    }
}
