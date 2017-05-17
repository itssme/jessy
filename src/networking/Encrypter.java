package networking;

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

    public Encrypter(String key_in) throws InvalidKeyException {
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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }

        return encrypted_string;
    }

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
            e.printStackTrace();
        }

        return decrypted;
    }
}