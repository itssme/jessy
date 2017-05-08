package networking;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.DoubleSummaryStatistics;

import static org.junit.jupiter.api.Assertions.*;

class EncrypterTest {
    @Test
    void test1() {

        try {
            Encrypter encrypter = new Encrypter("passwort");

            String encrypted = encrypter.encrypt("hallo");
            String decrypted = encrypter.decrypt(encrypted);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void test2() {
        boolean worked = true;

        try {
            for (int i = 0; i < 1000; i++) {
                String encrypt = Integer.toString(i);

                Encrypter encrypter = new Encrypter(encrypt);

                encrypt = Double.toString(Math.random());

                String encrypted = encrypter.encrypt(encrypt);
                String decrypted = encrypter.decrypt(encrypted);

                if (! encrypt.equals(decrypted)) {
                    worked = false;
                }
            }
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(worked);
    }
}