import java.lang.ProcessHandle;
import java.util.*;
import java.util.stream.*;
public class LockList {
    private ArrayList<String> lockedApps;
    public LockList() {
        lockedApps = new ArrayList<String>();
        lockedApps.add("国h中O我上现P&有HF好O淋A*S早MIu淇dhf87f冰87$!在#(&$&)" + Math.random() * Math.random());
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
                if (process.info().command().isPresent() && process.info().command().toString().indexOf(app) != -1) {
                    process.destroy();
                }
            }
        }
    }
    public ArrayList<String> retrieveProcesses() {
        ArrayList<String> processNames = new ArrayList<String>();
        Stream<ProcessHandle> processStream = ProcessHandle.allProcesses();
        ArrayList<ProcessHandle> processList = new ArrayList<ProcessHandle>(processStream.collect(Collectors.toList()));
        for (ProcessHandle process: processList) {
            if (process.info().command().isPresent()) {
                processNames.add(process.info().command().get());
            }
        }
        return processNames;
    }
}