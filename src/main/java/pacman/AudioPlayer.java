package pacman;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {

    private final URL startAudio = this.getClass().getResource("/main/resources/audio/Start.wav");
    private final URL wakaAudio = this.getClass().getResource("/main/resources/audio/Waka.wav");
    private final URL lifeAudio = this.getClass().getResource("/main/resources/audio/1UP.wav");
    private final URL fruitAudio = this.getClass().getResource("/main/resources/audio/Fruit.wav");
    private final URL ghostAudio = this.getClass().getResource("/main/resources/audio/Ghost.wav");
    private final URL powerPelletAudio = this.getClass().getResource("/main/resources/audio/PowerPellet.wav");
    private final URL intermissionAudio = this.getClass().getResource("/main/resources/audio/Intermission.wav");
    private final URL sirenAudio = this.getClass().getResource("/main/resources/audio/Siren.wav");
    private final URL eyesAudio = this.getClass().getResource("/main/resources/audio/Eyes.wav");
    private final URL deathAudio = this.getClass().getResource("/main/resources/audio/Death.wav");
    private AudioInputStream audioStream;
    private Clip startClip;
    private Clip wakaClip;
    private Clip lifeClip;  
    private Clip fruitClip; 
    private Clip ghostClip;
    private Clip powerPelletClip;
    private Clip intermissionClip;  // not yet
    private Clip sirenClip;
    private Clip eyesClip;
    private Clip deathClip;
    
    public void playStart() {
        if (startClip == null || startClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(startAudio);
                startClip = AudioSystem.getClip();
                startClip.open(audioStream);
                startClip.start();
            } catch (Exception e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    public void playLife() {
        if (lifeClip == null || lifeClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(lifeAudio);
                lifeClip = AudioSystem.getClip();
                lifeClip.open(audioStream);
                lifeClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playFruit() {
        if (fruitClip == null || fruitClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(fruitAudio);
                fruitClip = AudioSystem.getClip();
                fruitClip.open(audioStream);
                fruitClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playGhost() {
        try {
            audioStream = AudioSystem.getAudioInputStream(ghostAudio);
            ghostClip = AudioSystem.getClip();
            ghostClip.open(audioStream);
            ghostClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playPowerPellet() {
        if (powerPelletClip == null || powerPelletClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(powerPelletAudio);
                powerPelletClip = AudioSystem.getClip();
                powerPelletClip.open(audioStream);
                powerPelletClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loopPowerPellet(boolean bool) {
        if (powerPelletClip == null) {
            playPowerPellet();
        }
        if (bool == true) {
            powerPelletClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            powerPelletClip.stop();
        }
    }

    public void playIntermission() {
        if (intermissionClip == null || intermissionClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(intermissionAudio);
                intermissionClip = AudioSystem.getClip();
                intermissionClip.open(audioStream);
                intermissionClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playSiren() {
        if (sirenClip == null || sirenClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(sirenAudio);
                sirenClip = AudioSystem.getClip();
                sirenClip.open(audioStream);
                sirenClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loopSiren(boolean bool) {
        if (sirenClip == null) {
            playSiren();
        }
        if (bool == true) {
            sirenClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            sirenClip.stop();
        }
    }

    public void playEyes() {
        if (eyesClip == null || eyesClip.isRunning() == false) {
            try {
                audioStream = AudioSystem.getAudioInputStream(eyesAudio);
                eyesClip = AudioSystem.getClip();
                eyesClip.open(audioStream);
                eyesClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loopEyes(boolean bool) {
        if (eyesClip == null) {
            playEyes();
        }
        if (bool == true) {
            eyesClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else {
            eyesClip.stop();
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
                e.printStackTrace();
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
        return true;
    }
    
}
