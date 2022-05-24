package Pacman;

import java.io.File;
//import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
    //private final URL startAudio = this.getClass().getResource("./resources/audio/StartSound.wav");
    private final File startAudio = new File("./resources/audio/StartSound.wav");
    private final File wakaAudio = new File("./resources/audio/WakaSound.wav");
    private AudioInputStream audioStream;
    private Clip startClip;
    private Clip wakaClip;
    
    AudioPlayer() {
    }

    public void playStart() {
        try {
            audioStream = AudioSystem.getAudioInputStream(startAudio);
            startClip = AudioSystem.getClip();
            startClip.open(audioStream);
            startClip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void playWaka() {
        try {
            audioStream = AudioSystem.getAudioInputStream(wakaAudio);
            wakaClip = AudioSystem.getClip();
            wakaClip.open(audioStream);
            wakaClip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean isFinished(String sound) {
        if (sound == "start") {
            return !startClip.isRunning();
        }
        else if (sound == "waka"){
            return !wakaClip.isRunning();
        }
        return true;
    }
}
