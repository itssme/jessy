package sound;


import logging.LoggingSingleton;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Author:  Joel Klimont
 * Class:   3CHIF
 * Date:    24.05.17
 * Poject:  jessy_jfx
 * Package: utils
 * Desc.:   The Class responsible for Sound
 */
public class Sound {

    /**
     * Plays the specified Sound in the URL
     *
     * @param url The URL where the file is located
     * @return True, if the Thread was successfully started
     */
    public synchronized boolean playSound(final String url) {
        Thread t = new Thread(() -> {
            try {

                InputStream audioSrc = this.getClass().getResourceAsStream("sounds/" + url);
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);

                Clip clip = AudioSystem.getClip();
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
                LoggingSingleton.getInstance().warning("Could not play sound " + e.getMessage());
            }
        });
        t.start();
        return true;
    }
}
