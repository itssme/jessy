package utils;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    19.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:   A simple Observer for a playerSwitch
 */
public interface PlayerSwitchObserver {

    /**
     * This method gets executed every time the player changes
     *
     * @param canPlay A variable representing, whether the player can play
     */
    void onPlayerSwitch(final boolean canPlay);

}
