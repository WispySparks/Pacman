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
    private final File deathAudio = new File("./resources/audio/DeathSound.wav");
    private AudioInputStream audioStream;
    private Clip startClip;
    private Clip wakaClip;
    private Clip deathClip;
    private int x = 0;
    
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
        if (wakaClip == null || wakaClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(wakaAudio);
                wakaClip = AudioSystem.getClip();
                wakaClip.open(audioStream);
                wakaClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void playDeath() {
        if (x == 0) {
            try {
                audioStream = AudioSystem.getAudioInputStream(deathAudio);
                deathClip = AudioSystem.getClip();
                deathClip.open(audioStream);
                deathClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
            x = 1;
        }
    }

    public boolean isFinished(String sound) {
        if (sound == "start") {
            return !startClip.isRunning();
        }
        else if (sound == "waka"){
            return !wakaClip.isRunning();
        }
        else if (sound == "death") {
            return !deathClip.isRunning();
        }
        return true;
    }
}
