public class App {
    private String name;
    private String id;
    public App() {
        
    }

    public void outputToApp(String output) {
        if (System.getProperty("os.name").startsWith("Windows")) {
                //38 is where "?" appears if it does in winget output
                if (output.length() > 0) {
                    name = output.substring(0, 38).strip();
                    id = output.substring(40,  78).strip();
                }
        }
    }
    //consider making protected
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
}