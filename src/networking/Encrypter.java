package networking;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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

        try {
            // generate key
            this.key = key_in;
            aesKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String text) {
        String encrypted_string = null;

        try {
            // encryption
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            encrypted_string = new String(encrypted, "ISO-8859-1");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return encrypted_string;
    }

    public String decrypt(String encryptedStr) {
        String decrypted = null;

        byte[] encrypted = new byte[0];
        try {
            encrypted = encryptedStr.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            // decryption
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(encrypted));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }
}