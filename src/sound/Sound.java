package sound;


import logging.LoggingSingleton;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        this.getClass().getResourceAsStream("sounds/" + url));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.out.println(e);
                LoggingSingleton.getInstance().warning("Could not play sound " + e.getMessage());
            }
        });
        t.start();
        return true;
    }
}
