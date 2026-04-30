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
    private static int playlistIndex = 0;
    private static File selectedDirectory;
    public static void playMusic() {
        try {
            if (mediaPlayer != null) {
                return; // Prevent multiple players from starting
            }

            //relative path to audio file
            String musicPath = System.getProperty("user.dir") + "\\music\\prodappmusic.mp3";
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
        if (mediaPlayer != null)
            mediaPlayer.stop();
        
        playlistIndex = 0;
        selectedDirectory = dir;

        setNewMedia(selectedDirectory);
        //mediaPlayer.stop();
        mediaPlayer.setOnReady(new Runnable(){
            public void run() {
                mediaPlayer.play();
            }
        });
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (playlistIndex < selectedDirectory.length()) {
                    playlistIndex += 1;
                    setNewMedia(selectedDirectory);
                    mediaPlayer.play();
                }
                else {
                    mediaPlayer.stop();
                    return;
                }
            }
        });
    }

    public static long pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            TimeUnit t = TimeUnit.NANOSECONDS;
            currElapsedTime = t.toSeconds(currElapsedTime);
        }
        return currElapsedTime;
    }

    public static void changeTrack(boolean forward) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            if (forward) 
                playlistIndex++;
            else 
                playlistIndex--;

            if (playlistIndex < selectedDirectory.length()) 
                setNewMedia(selectedDirectory);
            else
                mediaPlayer.stop();
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
    
    private static void setNewMedia(File dir) {
        File f = dir.listFiles()[playlistIndex];
        if (f.isFile()) {
            currMedia = new Media(f.toURI().toString());
            mediaPlayer = new MediaPlayer(currMedia);
            mediaPlayer.setCycleCount(1);
            mediaPlayer.setVolume(1);
        }
    }
}