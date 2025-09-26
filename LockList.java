import java.lang.ProcessHandle;
import java.util.*;
import java.util.stream.*;
import java.lang.runtime.*;
import java.io.*;
//use winget list to potentially find all applications installed on device - need to filter certain things that are not typically accessed
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