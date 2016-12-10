package CipherGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class DeCrypter {

	private File commonWordFile;
	private Stack<String> commonWords;
	private int numWords = 0;
	private int maxWords = 0;
	private AlphabetMapping bestKeySoFar;
	private AlphabetMapping originalKey;

	// variables
	private FrequencyReader givenFrequencyMap;
	private FrequencyReader cipherFrequencyMap;
	private AlphabetMapping key;
	private File cipherFile;
	private String cipherText;
	private String plainText;

	FrequencyString[] givenFreq;
	FrequencyString[] cipherFreq;

	public DeCrypter() {
		key = new AlphabetMapping();
		cipherFile = null;
		cipherText = null;
		plainText = null;
		givenFrequencyMap = null;
		cipherFrequencyMap = null;
	}

	/**
	 * Constructor that takes the cipher in string form and a File for freq
	 *
	 * @param cipher String representing the ciphertext
	 * @param freq File representing the frequency
	 */
	public DeCrypter(String cipher, File freq) {
		key = new AlphabetMapping();
		cipherFile = null;
		givenFrequencyMap = new FrequencyReader(freq);
		cipherFrequencyMap = new FrequencyReader(cipher);

		cipherText = cipher;
		setSmartKey();
	}

	/**
	 * Constructor that takes two Files for the cipher and frequency
	 *
	 * @param cipher File representing the ciphertext
	 * @param freq File representing the frequency
	 */
	public DeCrypter(File cipher, File freq) {
		try {
			key = new AlphabetMapping();
			cipherFile = cipher;
			givenFrequencyMap = new FrequencyReader(freq);
			cipherFrequencyMap = new FrequencyReader(cipher);
			cipherText = "";

			Scanner in = new Scanner(cipherFile);

			in.useDelimiter("");

			while (in.hasNext()) {
				cipherText += in.next().toUpperCase();
			}
			setSmartKey();

		} catch (FileNotFoundException ex) {
			System.out.println("Cannot find this file.");
			System.exit(1);
		}
	}

	/**
	 * Method to set the frequency
	 *
	 * @param f File to represent the frequency
	 */
	public final void setFrequency(File f) {
		givenFrequencyMap = new FrequencyReader(f);
	}

	/**
	 * Method to generate the plaintext with the given key and ciphertext
	 */
	public void generatePlainText() {
		plainText = "";
		Scanner in = new Scanner(cipherText);
		in.useDelimiter("");
		while (in.hasNext()) {
			String str = in.next();

			for (int i = 0; i < str.length(); i++) {
				plainText += key.get(str.charAt(i));
			}
		}
	}

	public AlphabetMapping getKey() {
		return key;
	}

	/**
	 * Method to set the key from another AlphabetMapping
	 *
	 * @param a AlphabetMapping to assign to the key
	 */
	public final void setKey(AlphabetMapping a) {
		key = a;
		generatePlainText();
	}

	/**
	 * Method to set the plaintext character to map to the given ciphertext This
	 * method will exit the program if the set letter is already used
	 *
	 * @param c1 the ciphertext to assign to
	 * @param c2 the plaintext to assign
	 */
	public final void setChar(char c1, char c2) {
		key.setPlain(c1, c2);
	}

	/**
	 * Method to swap the plaintext character mappings of two ciphertext
	 * letters. This method will not exit and should be used to fix the setChar
	 * methods issues.
	 *
	 * @param c1 the first plaintext letter to swap
	 * @param c2 the second plaintext letter to swap
	 */
	public final void swapChar(char c1, char c2) {
		key.swapPlain(c1, c2);

		plainText = plainText.replaceAll(String.valueOf(c1), "REPLACE");
		plainText = plainText.replaceAll(String.valueOf(c2), String.valueOf(c1));
		plainText = plainText.replaceAll("REPLACE", String.valueOf(c1));
	}

	/**
	 * Method to generate the plaintext and return it
	 *
	 * @return String of plaintext generated
	 */
	public final String getPlainText() {
		return plainText;
	}

	/**
	 * Method to return the cipher text associated with this DeCrypter
	 *
	 * @return String of ciphertext
	 */
	public final String getCipherText() {
		return cipherText;
	}

	/**
	 * Method to generate a smart key based on generated frequency
	 */
	public final void setSmartKey() {

		key = new AlphabetMapping();

		PriorityQueue<FrequencyString> genFrequency = new PriorityQueue<>(new FrequencyComparator());
		PriorityQueue<FrequencyString> cipherFrequency = new PriorityQueue<>(new FrequencyComparator());
		givenFreq = givenFrequencyMap.getSingleLetterFrequency();
		cipherFreq = cipherFrequencyMap.getSingleLetterFrequency();

		for (int i = 0; i < 26; i++) {
			genFrequency.add(givenFreq[i]);
			cipherFrequency.add(cipherFreq[i]);
		}

		while (!genFrequency.isEmpty()) {
			setChar(cipherFrequency.remove().string().charAt(0), genFrequency.remove().string().charAt(0));
		}

		originalKey = new AlphabetMapping(key);
		//setSmarterKey();
	}

	private void setSmarterKey() {
		generatePlainText();

		commonWords = new Stack<>();
		try {
			Scanner in = new Scanner(commonWordFile);
			while (in.hasNext()) {
				commonWords.add(in.nextLine());
			}
		} catch (FileNotFoundException ex) {

		}

		// first permutation ((t), A, O, I, N, S, H, R)
		String original = "ETAOIN";

		permutation("", original, original);

		key = new AlphabetMapping(bestKeySoFar);
		maxWords = 0;

		// second permutation (D & L)
		original = "SHRDL";

		permutation("", original, original);

		key = new AlphabetMapping(bestKeySoFar);
		maxWords = 0;

		// third permutation (C, U, M, W, F, G)
		original = "CFGMUW";

		permutation("", original, original);

		key = new AlphabetMapping(bestKeySoFar);
		maxWords = 0;

		// fourth permutation (W, F, G, Y, P, B)
		original = "WFGYPB";

		permutation("", original, original);

		key = new AlphabetMapping(bestKeySoFar);

		generatePlainText();
		System.out.println(findCommonWords());
	}

	public FrequencyString[] getCipherFreq() {
		return cipherFreq;
	}

	public FrequencyString[] getGivenFreq() {
		return givenFreq;
	}

	private void permutation(String permutation, String str, String original) {
		int n = str.length();
		if (n == 0) {
			for (int i = 0; i < original.length(); i++) {
				setChar(originalKey.getCipher(original.charAt(i)), permutation.charAt(i));
			}
			numWords = findCommonWords();
			if (numWords >= maxWords) {
				maxWords = numWords;
				bestKeySoFar = new AlphabetMapping(key);
			}

		} else {
			for (int i = 0; i < n; i++) {
				permutation(permutation + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n), original);
			}
		}
	}

	private int findCommonWords() {
		int num = 0;
		
		generatePlainText();
		
		for (int i = 0; i < commonWords.size(); i++) {
			int index = 0;
			while (plainText.indexOf(commonWords.get(i), index) != -1) {
				num++;
				index = plainText.indexOf(commonWords.get(i).toLowerCase(), index) + commonWords.get(i).length();
			}
		}

		return num;
	}

	public static void main(String[] args) {
		GUI.main(args);
	}
}
