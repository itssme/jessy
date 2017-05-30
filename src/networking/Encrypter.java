package networking;

import logging.LoggingSingleton;
import main.ChessGameController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   Encrypt Strings
 */
public class Encrypter  {

    private String key;
    private Key aesKey;
    private Cipher cipher;
    private ChessGameController controller;

    /**
     * Checks the password and generates the key
     *
     * @param key_in the password the user typed in
     * @throws InvalidKeyException the password is not valid
     */
    public Encrypter(String key_in, ChessGameController controller) throws InvalidKeyException {
        this.controller = controller;

        if (key_in.length() <= 16) {
            // the password must have a lenght of 16
            // if no password is set the password will be 16*z

            while (key_in.length() < 16) {
                key_in = key_in + "z";
            }
        } else {
            throw new InvalidKeyException();
        }

        try { // generate key
            this.key = key_in;
            aesKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            LoggingSingleton.getInstance().severe("Error generating AES key " + e.getMessage());
        }
    }

    /**
     * Encrypts a <code>String</code>
     *
     * @param text the <code>String</code> to encrypt
     * @return the encrypted <code>String</code>
     */
    public String encrypt(String text) {
        String encrypted_string = null;

        try { // encryption
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }

            // the encrypted String
            String enc = sb.toString();
            encrypted_string = StringEncoder.encode(enc);

        } catch(Exception e) {
            LoggingSingleton.getInstance().severe("Error encrypting message " + e.getMessage());
            controller.disconnect();
        }

        return encrypted_string;
    }

    /**
     * Decrypt a <code>String</code>
     *
     * @param encryptedStr the encrypted <code>String</code>
     * @return the decrypted <code>String</code>
     */
    public String decrypt(String encryptedStr) {
        String decrypted = null;

        encryptedStr = StringEncoder.decode(encryptedStr);

        try { // decryption
            byte[] bb = new byte[encryptedStr.length()];
            for (int i=0; i<encryptedStr.length(); i++) {
                bb[i] = (byte) encryptedStr.charAt(i);
            }

            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(bb));

        } catch (Exception e) {
            LoggingSingleton.getInstance().severe("Error decrypting message " + e.getMessage());
            controller.disconnect();
        }

        return decrypted;
    }
}