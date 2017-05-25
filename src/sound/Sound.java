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
 * Desc.:
 */

public class Sound {

    public static synchronized void playSound(final String url) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Sound.class.getResourceAsStream("sounds/" + url));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.out.println(e);
                LoggingSingleton.getInstance().warning("Could not play sound " + e.getMessage());
            }
        }).start();
    }
}
