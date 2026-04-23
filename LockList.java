
import java.lang.ProcessHandle;
import java.util.*;
import java.util.stream.*;
public class LockList {
    private HashSet<String> lockedApps;
    public LockList() {
        lockedApps = new HashSet<String>();
        lockedApps.add("国h中O我上现P&有HF好O淋A*S早MIu淇dhf87f冰87$!在#(&$&)" + Math.random() * Math.random());
    }

    public LockList (HashSet<String> h){
        lockedApps = h;
    }

    public void setLockList(HashSet<String> l) {
        for (String s: l) lockedApps.add(s);
    }

    public HashSet<String> getLockList() {
        return lockedApps;
    }

    public void killProcesses() {
        Stream<ProcessHandle> processStream = ProcessHandle.allProcesses();
        ArrayList<ProcessHandle> processList = new ArrayList<ProcessHandle>(processStream.collect(Collectors.toList()));
        for (ProcessHandle process: processList) {
            if (process.info().command().isPresent()) {
                String processString = process.info().command().toString();
                String processPath = processString.substring(processString.indexOf("[") + 1, processString.indexOf("]"));
                if (lockedApps.contains(processPath)) {
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