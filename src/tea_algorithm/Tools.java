package tea_algorithm;

import java.math.BigInteger;

public class Tools {

    // Method to convert byte[] to Integer[]
    public static Integer[] convertFromBytesToInts(byte[] bs) {

        // pad the string with spaces (8 bytes in a 64-bit block)
        int padding = (bs.length % 8 != 0) ? (8 - bs.length % 8) : 0;

        byte[] bsPadded = new byte[bs.length + padding];
        Integer[] ints = new Integer[bsPadded.length / 4];

        System.arraycopy(bs, 0, bsPadded, 0, bs.length);
        for (int i = 0; i < padding; i++) {
            bsPadded[bs.length + i] = Byte.valueOf("32"); // 32 is the ASCII code for space
        }

        for (int i = 0; i < ints.length; i++) {

            byte b0 = bsPadded[i * 4 + 0];
            byte b1 = bsPadded[i * 4 + 1];
            byte b2 = bsPadded[i * 4 + 2];
            byte b3 = bsPadded[i * 4 + 3];

            Integer temp = ((0xFF & b0) << 24)
                    | ((0xFF & b1) << 16)
                    | ((0xFF & b2) << 8)
                    | ((0xFF & b3));

            ints[i] = temp;
        }

        return ints;
    }

    // Method to convert a String of hex characters to Integer[]
    public static Integer[] convertFromHexStringToInts(String s) {

        // pad the string with zeroes at the end (16 hex character blocks)
        int padding = (s.length() % 16 != 0) ? (16 - s.length() % 16) : 0;
        for (int i = 0; i < padding; i++) {
            s += "0";
        }

        // Integers must be a multiple of 2 (8 hex chars per Integer)
        Integer[] ints = new Integer[(s.length() / 8)];

        for (int i = 0; i < ints.length; i++) {
            BigInteger bp = new BigInteger(s.substring((i * 8), (i * 8) + 8), 16);
            ints[i] = bp.intValue();
        }

        return ints;
    }

    // Method to convert Integer[] to byte[]
    public static byte[] convertFromIntsToBytes(Integer[] ints) {

        byte[] bs = new byte[ints.length * 4];

        // loop through all Integers converting to byte array
        for (int i = 0; i < ints.length; i++) {
            // 4-byte array for current Integer
            byte[] temp = intToByte(ints[i]);

            bs[i * 4 + 0] = temp[0];
            bs[i * 4 + 1] = temp[1];
            bs[i * 4 + 2] = temp[2];
            bs[i * 4 + 3] = temp[3];
        }

        return bs;
    }

    // Method to convert Integer[] to a String of hex characters
    public static String convertFromIntsToHexString(Integer[] ints) {

        String hexString = "";

        for (int i = 0; i < ints.length; i++) {
            // format the string to be uppercase and add the zeroes in front
            String temp = String.format("%8s", Integer.toHexString(ints[i]));
            temp = temp.toUpperCase();
            temp = temp.replaceAll(" ", "0");

            hexString += temp;
        }

        return hexString;
    }

    // Method to convert an individual Integer to a 4-byte array
    private static byte[] intToByte(Integer a) {
        byte[] ret = new byte[4];

        ret[3] = (byte) ((a) & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);

        return ret;
    }
}
