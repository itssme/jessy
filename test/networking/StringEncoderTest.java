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

    @Test
    void test3() {
        String testString = "ÜÄÖüäö!§$%&/()=?³²¹³¼½¬{[]}@ł€¶ŧ←↓→øþłĸŋđðſæ»«¢„“”µ,.·…:;-_";

        String encodedString = StringEncoder.encode(testString);
        String decodedString = StringEncoder.decode(encodedString);

        assertTrue(testString.equals(decodedString));
    }

    @Test
    void test4() {
        boolean worked = true;

        try {
            for (int i = 0; i < 1000; i++) {
                String encode = Integer.toString(i);

                String encoded = StringEncoder.encode(encode);
                String decoded = StringEncoder.decode(encoded);

                if (! encode.equals(decoded)) {
                    worked = false;
                }
            }
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(worked);
    }

}