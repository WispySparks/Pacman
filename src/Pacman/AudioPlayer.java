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
    
    AudioPlayer() {
    }

    public void playStart() {
        if (startClip == null || startClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(startAudio);
                startClip = AudioSystem.getClip();
                startClip.open(audioStream);
                startClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
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
        if (deathClip == null || deathClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(deathAudio);
                deathClip = AudioSystem.getClip();
                deathClip.open(audioStream);
                deathClip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public boolean isFinished(String sound) {
        if (sound == "start") {
            if (startClip != null) {
                return !startClip.isRunning();
            }
        }
        else if (sound == "waka"){
            if (wakaClip != null) {
                return !wakaClip.isRunning();
            }
        }
        else if (sound == "death") {
            if (deathClip != null) {
                return !deathClip.isRunning();
            }
        }
        return false;
    }
}
