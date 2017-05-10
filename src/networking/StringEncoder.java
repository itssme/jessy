package networking;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-04-10
 * Project: jessy
 * Desc.:   Encode and decode strings
 */
public class StringEncoder {
    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    public static String decode(String toDecode) {
        byte[] bytesDecoded = decoder.decode(toDecode.getBytes());
        String decoded = new String(bytesDecoded);
        return decoded;
    }

    public static String encode(String toEncode) {
        byte[] bytesEncoded = encoder.encode(toEncode.getBytes());
        String encoded = new String(bytesEncoded);
        return encoded;
    }
}