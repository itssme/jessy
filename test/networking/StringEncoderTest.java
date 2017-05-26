package networking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by joel on 25.05.17.
 */
class StringEncoderTest {
    @Test
    void test1() {
        String testString = "Hello";

        String encodedString = StringEncoder.encode(testString);
        String decodedString = StringEncoder.decode(encodedString);

        assertTrue(testString.equals(decodedString));
    }

    @Test
    void test2() {
        String testString = "Hallo, this is a standard chess message! Have fun playing the game!";

        String encodedString = StringEncoder.encode(testString);
        String decodedString = StringEncoder.decode(encodedString);

        assertTrue(testString.equals(decodedString));
    }

}