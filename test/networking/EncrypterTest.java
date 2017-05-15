package networking;

import org.junit.jupiter.api.Test;


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
                    System.out.println(decrypted + " -> " + encrypt);
                    worked = false;
                }
            }
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(worked);
    }

    @Test
    void test3() {
        try {
            Encrypter encrypter = new Encrypter("password");

            String encrypted = encrypter.encrypt("wwww");
            String decrypted = encrypter.decrypt(encrypted);

        } catch (Exception e) {
            assertTrue(false);
        }

    }

    @Test
    void test4() {
        try {
            Encrypter encrypter = new Encrypter("12sicher21");

            String encrypted = encrypter.encrypt("how are you?");
            String decrypted = encrypter.decrypt(encrypted);

        } catch (Exception e) {
            assertTrue(false);
        }

    }
}