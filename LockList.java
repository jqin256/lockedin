import java.lang.ProcessHandle;
import java.util.*;
import java.util.stream.*;
public class LockList {
    private ArrayList<String> lockedApps;

    public LockList() {
        lockedApps = new ArrayList<String>();
        lockedApps.add("hOE&HFOA*SMIudhf87f87$!#(&$&)早上好中国我现在有冰淇淋" + Math.random() * Math.random());
    }

    public LockList (ArrayList<String> l){
        lockedApps = l;
    }

    public void setLockList(ArrayList<String> l) {
        lockedApps = l;
    }

    public ArrayList<String> getLockList() {
        return lockedApps;
    }

    public void killProcesses() {
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