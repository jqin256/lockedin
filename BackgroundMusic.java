import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;



public class BackgroundMusic {
    
    private static MediaPlayer mediaPlayer;

    public static void playMusic() {
        try {
            if (mediaPlayer != null) {
                return; // Prevent multiple players from starting
            }

            // ABSOLUTE PATH to audio file
            String musicPath = "C:\\Users\\Rithik_DHS\\Downloads\\prodappmusic.mp3";

            Media sound = new Media(new File(musicPath).toURI().toString());
            mediaPlayer = new MediaPlayer(sound); 

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //loops forever
            mediaPlayer.setVolume(0.25); //sets music volume

            mediaPlayer.play();

        } catch (Exception e) {
            System.out.println("Error loading background music: " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}
