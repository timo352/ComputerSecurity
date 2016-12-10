package CipherGUI;

public class AlphabetMapping {

    /**
     * Helper class that represents a single character key
     */
    private static class Mapping {

        private char cipher;
        private char plain;

        private Mapping(char c) {
            cipher = c;
            plain = c;
        }

        private Mapping(char c1, char c2) {
            cipher = c1;
            plain = c2;
        }
    }

    private Mapping[] keyboard;

    /**
     * Default constructor that initialized the mapping to blank characters
     */
    public AlphabetMapping() {
        keyboard = new Mapping[26];
        char c = 'A';
        for (int i = 0; i < 26; i++) {
            keyboard[i] = new Mapping(c);
            c++;
        }
    }

    /**
     * Constructor that functions like a clone of an AlphabetMapping
     *
     * @param a
     */
    public AlphabetMapping(AlphabetMapping a) {
        this();
        for (int i = 0; i < 26; i++) {
            keyboard[i] = new Mapping(keyboard[i].cipher, a.keyboard[i].plain);
        }
    }

	public void setPlain(char c1, char c2){
		
		c1 = Character.toUpperCase(c1);
		c2 = Character.toLowerCase(c2);
		
		for (int i = 0; i < 26; i++) {
            if (keyboard[i].cipher == c1){
				keyboard[i].plain = c2;
			}
        }
	}
	
	
    /**
     * swapPlain is a method to switch to plaintext assignments. Unlike setChar
     * this one will not exit the program and should be used to help setChar
     *
     * @param c1 the first plaintext letter to swap
     * @param c2 the second plaintext letter to swap
     */
    public void swapPlain(char c1, char c2) {
		
		c1 = Character.toLowerCase(c1);
		c2 = Character.toLowerCase(c2);
		
        for (int i = 0; i < 26; i++) {
            if (keyboard[i].plain == c1) {
                keyboard[i].plain = c2;
            } else if (keyboard[i].plain == c2) {
                keyboard[i].plain = c1;
            }
        }
    }

    /**
     * get is a method to return the plaintext equivalent of the given cipher
     * character
     *
     * @param c the cipher letter that you would like the
     * @return the plaintext character
     */
    public char get(char c) {

        if (!Character.isLetter(c)) {
            return c;
        }
        c = Character.toUpperCase(c);

        for (int i = 0; i < 26; i++) {
            if (keyboard[i].cipher == c) {
                return keyboard[i].plain;
            }
        }

        return '\0';
    }

    public char getCipher(char c) {
        if (!Character.isLetter(c)) {
            return c;
        }
        c = Character.toLowerCase(c);

        for (int i = 0; i < 26; i++) {
            if (keyboard[i].plain == c) {
                return keyboard[i].cipher;
            }
        }

        return '\0';
    }
}
