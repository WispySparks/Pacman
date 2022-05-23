package Pacman;

import java.io.File;
//import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
    //private final URL startAudio = this.getClass().getResource("./resources/audio/StartSound.wav");
    private final File fileAudio = new File("./resources/audio/StartSound.wav");
    private AudioInputStream audioStream;
    private Clip clip;
    
    AudioPlayer() {
    }

    public void playStart() {
        try {
            audioStream = AudioSystem.getAudioInputStream(fileAudio);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isFinished() {
        return !clip.isRunning();
    }
}
