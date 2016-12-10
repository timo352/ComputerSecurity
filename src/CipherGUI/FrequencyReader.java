package CipherGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FrequencyReader {

    private final FrequencyString[] frequency;
    private final FrequencyString[] doubleFrequency;
    private final File freqFile;
    private final String freqString;

    /**
     * Constructor for a FrequencyReader that take the generated frequency as a
     * File
     *
     * @param f File representing the generated frequency
     */
    public FrequencyReader(File f) {
        freqFile = f;
        freqString = null;
        frequency = new FrequencyString[26];
        doubleFrequency = new FrequencyString[676];

        char c1 = 'A';
        for (int i = 0; i < 26; i++) {
            char c2 = 'A';
            frequency[i] = new FrequencyString(Character.toString(c1));
            for (int j = 0; j < 26; j++) {
                doubleFrequency[(i * 26) + j] = new FrequencyString(Character.toString(c1) + Character.toString(c2));
                c2++;
            }
            c1++;
        }

        generateLetterFrequencies();
    }

    /**
     * Constructor for a FrequencyReader that take the generated frequency as a
     * String
     *
     * @param str String representing the generated frequency
     */
    public FrequencyReader(String str) {
        freqFile = null;
        freqString = str;

        frequency = new FrequencyString[26];
        doubleFrequency = new FrequencyString[676];

        char c1 = 'A';
        for (int i = 0; i < 26; i++) {
            char c2 = 'A';
            frequency[i] = new FrequencyString(Character.toString(c1));
            for (int j = 0; j < 26; j++) {
                doubleFrequency[(i * 26) + j] = new FrequencyString(Character.toString(c1) + Character.toString(c2));
                c2++;
            }
            c1++;
        }

        generateLetterFrequencies();
    }

    /**
     * Function to read in the frequency and assign each character the number of
     * times it appears.
     */
    private void generateLetterFrequencies() {

        try {
            String entireFrequency = "";
            Scanner in;
            if (freqString == null) {
                in = new Scanner(freqFile);
            } else {
                in = new Scanner(freqString);
            }

            String str;
            while (in.hasNext()) {
                str = in.nextLine();
                entireFrequency += str;
                for (int i = 0; i < str.length(); i++) {

                    char c = Character.toUpperCase(str.charAt(i));

                    if (Character.isLetter(c)) {
                        frequency[c - 65].incrTimes();
                    }

                    if (i > 0 && Character.isLetter(c) && Character.isLetter(str.charAt(i - 1))) {
                        char c2 = Character.toUpperCase(str.charAt(i - 1));

                        doubleFrequency[(c2 - 65) * 26 + (c - 65)].incrTimes();
                    }
                }
            }
            
            for(int i=0; i<frequency.length; i++){
                frequency[i].setPercent(entireFrequency.length());
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Cannot read from this file.");
            System.exit(1);
        }
    }

    /**
     * Method to return the frequency of all characters
     *
     * @return FrequencyString[] of all letter frequencies
     */
    public FrequencyString[] getSingleLetterFrequency() {
        return frequency;
    }

    /**
     * Method to return the frequency of all two-letter combinations
     *
     * @return FrequencyString[] of all two-letter frequencies
     */
    public FrequencyString[] getDoubleLetterFrequency() {
        return doubleFrequency;
    }
}
