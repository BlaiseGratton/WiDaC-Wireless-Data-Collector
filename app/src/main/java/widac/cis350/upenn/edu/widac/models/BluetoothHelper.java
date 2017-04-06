package widac.cis350.upenn.edu.widac.models;

/**
 * Created by J. Patrick Taggart on 4/2/2017.
 */

public class BluetoothHelper {
    // Parse the data received from scale
    public static int parseBytesNutriscale(byte[] bytes) {
        int sign = 1;
        int value = 0;
        if (bytes[0] < 0) {
            sign = -1;
        }

        // bits 12-0 gives value of the scale
        value = (bytes[0] << 4) * 2^7 + bytes[1];
        return value;
    }

}
