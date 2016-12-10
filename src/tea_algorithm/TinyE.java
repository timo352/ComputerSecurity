package tea_algorithm;

public class TinyE {

    private static final int DELTA = 0x9e3779b9;
    private static final int NUM_ROUNDS = 32;

    public static enum Mode {

        ECB, CBC, CTR
    };

    public Integer[] encrypt(Integer[] plaintext, Integer[] key, Mode mode, Integer[] iv) {

        Integer[] ciphertext = new Integer[plaintext.length];

        // CBC blockChain (initialize to IV for first block)
        Integer[] blockChain = iv;

        // loop through plaintext (two at a time for L & R)
        for (int i = 0; i < plaintext.length; i += 2) {
            Integer L = 0;
            Integer R = 0;

            // Assign L & R based on mode
            switch (mode) {
                case ECB:
                    L = plaintext[i];
                    R = plaintext[i + 1];
                    break;
                case CBC:
                    L = plaintext[i] ^ blockChain[0];
                    R = plaintext[i + 1] ^ blockChain[1];
                    break;
                case CTR:
                    L = iv[0];
                    R = iv[1];
                    break;
            }

            Integer sum = 0;
            for (int j = 0; j < NUM_ROUNDS; j++) {
                sum += DELTA;
                L += ((R << 4) + key[0]) ^ (R + sum) ^ ((R >> 5) + key[1]);
                R += ((L << 4) + key[2]) ^ (L + sum) ^ ((L >> 5) + key[3]);
            }

            // modify special variables for CBC or CTR mode
            switch (mode) {
                case CBC:
                    blockChain[0] = L;
                    blockChain[1] = R;
                    break;
                case CTR:
                    L = L ^ plaintext[i];
                    R = R ^ plaintext[i + 1];

                    // increment IV (turn it into a 64-bit number first)
                    long temp = (long) iv[0] << 32 | iv[1];
                    temp++;

                    iv[0] = (int) (temp >> 32);
                    iv[1] = (int) (temp);
                    break;
            }

            // assign the ciphertext values
            ciphertext[i] = L;
            ciphertext[i + 1] = R;
        }

        return ciphertext;
    }

    public Integer[] decrypt(Integer[] ciphertext, Integer[] key, Mode mode, Integer[] iv) {

        Integer[] plaintext = new Integer[ciphertext.length];

        // CBC blockChain (initialize to IV for first block)
        Integer[] blockChain = iv;

        // In CTR mode, instead of decrypting we encrypt again.
        if (mode == Mode.CTR) {
            return encrypt(ciphertext, key, mode, iv);
        } else {

            // loop through all of the ciphertext (two at a time for L & R)
            for (int i = 0; i < ciphertext.length; i += 2) {
                Integer L = ciphertext[i];
                Integer R = ciphertext[i + 1];
                
                Integer sum = DELTA << 5;
                for (int j = 0; j < NUM_ROUNDS; j++) {
                    R -= ((L << 4) + key[2]) ^ (L + sum) ^ ((L >> 5) + key[3]);
                    L -= ((R << 4) + key[0]) ^ (R + sum) ^ ((R >> 5) + key[1]);
                    sum -= DELTA;
                }
                
                // modify special variables for CBC mode
                if (mode == Mode.CBC) {
                    L = L ^ blockChain[0];
                    R = R ^ blockChain[1];

                    blockChain[0] = ciphertext[i];
                    blockChain[1] = ciphertext[i + 1];
                }

                // assign the plaintext values
                plaintext[i] = L;
                plaintext[i + 1] = R;
            }
        }
        return plaintext;

    }

}
