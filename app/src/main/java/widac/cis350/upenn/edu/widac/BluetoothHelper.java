package widac.cis350.upenn.edu.widac;

import static android.R.attr.x;

/**
 * Created by J. Patrick Taggart on 4/2/2017.
 */

public class BluetoothHelper {
    // Parse the data received from scale
    public static int parseBytesNutriscale(byte[] bytes, int size) {
        int sign = 1;
        int value;
        if (bytes[size-2] > 0) {
            sign = -1;
        }

        // bits 12-0 gives value of the scale
        value = ((bytes[size-2] & 0xf) * 256) + (((int) bytes[size-1]) & 0xff);
        return sign * value;
    }

}
