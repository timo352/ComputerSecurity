package passwordcracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordCrackerThread_BASIC extends PasswordCrackerThread {

	
	ArrayList<String> wordPerms = new ArrayList<>();
	ArrayList<String> acrPerms = new ArrayList<>();
	
	public PasswordCrackerThread_BASIC(String nm, List<String> dict) {

		super(nm, dict);
		
		try {
			Scanner in = new Scanner(new File("words.txt"));
			Scanner in2 = new Scanner(new File("abbrevs.txt"));
			
			while(in.hasNext()){
				String str = in.next();				
				
				
			//	wordPerms.add(str.toLowerCase());
				//wordPerms.add(str.charAt(0)+str.substring(1).toLowerCase());
			}
			
			while(in2.hasNext()){
				String str = in2.next();
				
				acrPerms.add(str.toLowerCase());
				acrPerms.add(str.toUpperCase());
				
			}
			
		} catch (FileNotFoundException ex) {
			
		}
		
		
	}

	public void run() {

		startTime = System.currentTimeMillis();

		// *** BASIC DICTIONARY WORD TESTS ***
		for (int i = 0; i < dictionary.size(); i++) {

			String dictionaryWord = dictionary.get(i);
			
			PasswordRunner.checkUsersForHash(dictionaryWord, startTime);
			
		}
		
		for (int i = 0; i < dictionary.size(); i++) {

			String dictionaryWord = dictionary.get(i);
			
			ArrayList<String> subPerm = WordPermuters.substitutionPermuter(dictionaryWord);
			ArrayList<String> appPerm = WordPermuters.appendNumberPermuter(dictionaryWord, 1234);
			ArrayList<String> prePerm = WordPermuters.prependNumberPermuter(dictionaryWord, 1980);
			
			for(String word : subPerm){
				PasswordRunner.checkUsersForHash(word, startTime);
			}
			
			for(String word : appPerm){
				PasswordRunner.checkUsersForHash(word, startTime);
			}
			
			for(String word : prePerm){
				PasswordRunner.checkUsersForHash(word, startTime);
			}
			
		}
	}
}
