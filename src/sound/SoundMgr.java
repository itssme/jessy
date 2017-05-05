package sound;

import logging.LoggingSingleton;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    02.05.17
 * Poject:  jessy
 * Package: sound
 * Desc.:
 */
public class SoundMgr extends Thread{

    public SoundMgr(String url) {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(url));
        } catch (UnsupportedAudioFileException e) {
            LoggingSingleton.getInstance().logToFile(Level.SEVERE, e.getLocalizedMessage());
        } catch (IOException e) {
            LoggingSingleton.getInstance().logToFile(Level.SEVERE, e.getLocalizedMessage());
        }
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(audioInputStream);
            clip.start();
        } catch (LineUnavailableException e) {
            LoggingSingleton.getInstance().logToFile(Level.SEVERE, e.getLocalizedMessage());
        } catch (IOException e) {
            LoggingSingleton.getInstance().logToFile(Level.SEVERE, e.getLocalizedMessage());
        }
    }

}