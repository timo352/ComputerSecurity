package passwordcracker;

import java.util.ArrayList;
import java.util.HashMap;

public class WordPermuters {

	public static ArrayList<String> appendNumberPermuter(String word, int maxNum) {
		ArrayList<String> numberAppend = new ArrayList<>();

		numberAppend.add(word);

		for (int i = 0; i <= maxNum; i++) {
			numberAppend.add(word + (Integer.toString(i)));
		}

		return numberAppend;
	}

	public static ArrayList<String> prependNumberPermuter(String word, int maxNum) {
		ArrayList<String> numberPrepend = new ArrayList<>();

		numberPrepend.add(word);

		for (int i = 0; i <= maxNum; i++) {
			numberPrepend.add((Integer.toString(i)) + word);
		}

		return numberPrepend;
	}

	public static ArrayList<String> appendCharacterPermuter(String word, int maxLength) {
		ArrayList<String> charAppend = new ArrayList<>();

		charAppend.add(word);

		if (maxLength > 0) {
			// add one character
			for (int i = 33; i <= 126; i++) {
				char a = (char) i;
				charAppend.add(word + String.valueOf(a));
			}
		}
		if (maxLength > 1) {
			// add two characters
			for (int i = 33; i <= 126; i++) {
				char a = (char) i;

				for (int j = 33; j <= 126; j++) {
					char b = (char) j;

					charAppend.add(word + String.valueOf(a) + String.valueOf(b));
				}
			}
		}
		if (maxLength > 2) {
			// add three characters
			for (int i = 33; i <= 126; i++) {
				char a = (char) i;

				for (int j = 33; j <= 126; j++) {
					char b = (char) j;

					for (int k = 33; k <= 126; k++) {
						char c = (char) k;

						charAppend.add(word + String.valueOf(a) + String.valueOf(b) + String.valueOf(c));
					}
				}
			}
		}
		return charAppend;
	}

	public static ArrayList<String> prependCharacterPermuter(String word, int maxLength) {
		ArrayList<String> charPrepend = new ArrayList<>();

		charPrepend.add(word);

		if (maxLength > 0) {
			// add one character
			for (int i = 33; i <= 126; i++) {
				char a = (char) i;
				charPrepend.add(String.valueOf(a) + word);
			}
		}
		if (maxLength > 1) {
			// add two characters
			for (int i = 33; i <= 126; i++) {
				char a = (char) i;

				for (int j = 33; j <= 126; j++) {
					char b = (char) j;

					charPrepend.add(String.valueOf(a) + String.valueOf(b) + word);
				}
			}
		}
		if (maxLength > 2) {
			// add three characters
			for (int i = 33; i <= 126; i++) {
				char a = (char) i;

				for (int j = 33; j <= 126; j++) {
					char b = (char) j;

					for (int k = 33; k <= 126; k++) {
						char c = (char) k;

						charPrepend.add(String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + word);
					}
				}
			}
		}
		return charPrepend;
	}

	public static ArrayList<String> substitutionPermuter(String word) {
		ArrayList<String> substitutions = new ArrayList<>();
		HashMap<String, String> subHash = new HashMap<>();
		String perm = word;

		for (int i = 0; i < 256; i++) {

			String str = String.format("%8s", Integer.toBinaryString(i)).replaceAll(" ", "0");

			if (str.charAt(0) == '1') {
				word = word.replaceAll("[Ii]", "1");
			}

			if (str.charAt(1) == '1') {
				word = word.replaceAll("[Ii]", "!");
			}

			if (str.charAt(2) == '1') {
				word = word.replaceAll("[Ee]", "3");
			}

			if (str.charAt(3) == '1') {
				word = word.replaceAll("[Aa]", "@");
			}

			if (str.charAt(4) == '1') {
				word = word.replaceAll("[Hh]", "#");
			}

			if (str.charAt(5) == '1') {
				word = word.replaceAll("[Oo]", "0");
			}

			if (str.charAt(6) == '1') {
				word = word.replaceAll("[Ss]", "5");
			}

			if (str.charAt(7) == '1') {
				word = word.replaceAll("[Ss]", "\\$");
			}

			if (!subHash.containsKey(word)) {
				substitutions.add(word);
				subHash.put(word, word);
			}
			word = perm;
		}

		return substitutions;
	}

	public static ArrayList<String> capitalizationPermuter(String word) {

		ArrayList<String> capPerm = new ArrayList<>();

		int size = (int) Math.pow(2, word.length());
		for (int i = 0; i < size; i++) {

			String test = String.format("%" + String.valueOf(word.length()) + "s", Integer.toBinaryString(i)).replaceAll(" ", "0");
			String addWord = word;
			for (int j = 0; j < word.length(); j++) {

				if (test.charAt(j) == '1') {

					if (j > 0 && j < word.length() - 1) {
						addWord = addWord.substring(0, j) + Character.toUpperCase(addWord.charAt(j)) + addWord.substring(j + 1);
					} else if (j == 0) {
						addWord = Character.toUpperCase(addWord.charAt(j)) + addWord.substring(j + 1);
					} else if (j == word.length() - 1) {
						addWord = addWord.substring(0, j) + Character.toUpperCase(addWord.charAt(j));
					}
				}
			}

			if (!capPerm.contains(addWord)) {
				capPerm.add(addWord);
			}
		}

		return capPerm;
	}

	public static ArrayList<String> abcPermuter(String word) {

		ArrayList<String> abcPerm = new ArrayList<>();

		abcPerm.add(word + "");
		abcPerm.add(word + "a");
		abcPerm.add(word + "ab");
		abcPerm.add(word + "abc");
		abcPerm.add(word + "abcd");
		abcPerm.add(word + "abcde");
		abcPerm.add(word + "abcdef");
		abcPerm.add(word + "abcdefg");
		abcPerm.add(word + "abcdefgh");
		abcPerm.add(word + "abcdefgh");
		abcPerm.add(word + "abcdefghi");
		abcPerm.add(word + "abcdefghij");
		abcPerm.add(word + "abcdefghijk");
		abcPerm.add(word + "abcdefghijkl");
		abcPerm.add(word + "abcdefghijklm");
		abcPerm.add(word + "abcdefghijklmn");

		return abcPerm;
	}
	
	public static ArrayList<String> lauraPermuter(String word) {

		ArrayList<String> lauraPerm = new ArrayList<>();

		for(int i=0; i<=12; i++){
			
			for(int j=0; j<=31; j++){
				
				for(int k = 1980; k<2016; k++){
					
					lauraPerm.add(word + String.valueOf(i) + String.valueOf(j) + String.valueOf(k));
					
				}
				
			}
			
		}
		

		return lauraPerm;
	}

}
