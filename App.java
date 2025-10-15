public class App {
    private String name;
    private String id;
    public App() {
        
    }

    protected void outputToApp(String output) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            if ((output.indexOf("  ") < output.indexOf("? ")) || output.indexOf("? ") == -1) {
                name = output.substring(0, output.indexOf("  "));
                output = output.substring(output.indexOf("  ") + 2);
                
            }
            else {
                name = output.substring(0, output.indexOf("? "));
                id = output.substring(output.indexOf("? ") + 2, output.substring(output.indexOf("? ") + 2).indexOf(Math.min(output.indexOf("  "), output.indexOf("? "))));
            }
        }
    }

    protected String getName() {
        return name;
    }
    protected String getId() {
        return id;
    }
}