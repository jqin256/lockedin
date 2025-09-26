import java.lang.ProcessHandle;
import java.util.*;
import java.util.stream.*;
public class LockList {
    private ArrayList<String> lockedApps;
    public LockList (ArrayList<String> l){
        lockedApps = l;
    }

    public ArrayList<String> getLockList() {
        return lockedApps;
    }

    public void killProcess() {
        Stream<ProcessHandle> processStream = ProcessHandle.allProcesses();
        ArrayList<ProcessHandle> processList = new ArrayList<ProcessHandle>(processStream.collect(Collectors.toList()));
        for (String app: this.lockedApps) {
            for (ProcessHandle process: processList) {
                if (process.info().toString().indexOf(app) > -1) {
                    process.destroy();
                }
            }
        }
    }
}