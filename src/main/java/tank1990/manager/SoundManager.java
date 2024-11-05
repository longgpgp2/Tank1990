package tank1990.manager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundManager {
    private Clip clip;
    public void soundLoader(String filePath){
        try{
            File sound = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playSound() {
        if ( clip != null) {
            clip.start();
        }
    }
    public void playLoop(){
        if ( clip != null){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stopSound(){
        if (clip != null && clip.isRunning()){
            clip.stop();
            clip.setFramePosition(0);
        }
    }
    public void resetSound(){
        if (clip != null){
            clip.setFramePosition(0);
        }
    }
    public void setSoundOn(boolean soundOn){
        if (!soundOn){
            stopSound();
        }else {
            playLoop();
        }
    }


}
