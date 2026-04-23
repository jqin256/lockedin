import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.concurrent.*;

public class BackgroundMusic {
    
    private static MediaPlayer mediaPlayer;
    private static Media currMedia;
    private static long currElapsedTime;
    private static long currStartTime;
    private static short move = 0;

    public static void playMusic() {
        try {
            if (mediaPlayer != null) {
                return; // Prevent multiple players from starting
            }

            //relative path to audio file
            String musicPath = "music\\prodappmusic.mp3";

            Media sound = new Media(new File(musicPath).toURI().toString());
            mediaPlayer = new MediaPlayer(sound); 

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //loops forever
            mediaPlayer.setVolume(0.25); //sets music volume

            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    public static void playPlaylist(File dir, boolean repeat) {
        mediaPlayer.stop();
        do {
            for (int i = 0; i < dir.listFiles().length; ++i) {
                File f = dir.listFiles()[i];
                if (f.isFile()) {
                    currMedia = new Media(f.toURI().toString());
                    mediaPlayer = new MediaPlayer(currMedia);
                    mediaPlayer.setCycleCount(1);
                    mediaPlayer.play();
                    currStartTime = System.nanoTime();
                    while (mediaPlayer.getCurrentCount() < 1) {
                        if (move == 1) {
                            break;
                        }
                        else if (move == -1) {
                            i -= 2;
                            break;
                        }
                        currElapsedTime = System.nanoTime() - currStartTime;
                    };
                    move = 0;
                    mediaPlayer.stop();
                }
            }

        } while(repeat);
    }

    public static long pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            TimeUnit t = TimeUnit.NANOSECONDS;
            currElapsedTime = t.toSeconds(currElapsedTime);
        }
        return currElapsedTime;
    }

    public static void setMove(short x) {
        move = x;
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}
