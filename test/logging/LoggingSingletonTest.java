package logging;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    27.05.17
 * Poject:  jessy
 * Package: logging
 * Desc.:
 */
class LoggingSingletonTest {
    @Test
    void getInstance() {
        File f = new File("jessy.log.xml");
        assert !f.exists();

        LoggingSingleton.getInstance().info("From Test");

        f = new File("jessy.log.xml");
        assert f.exists();
    }


}