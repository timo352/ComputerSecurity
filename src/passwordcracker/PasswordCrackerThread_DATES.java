package passwordcracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordCrackerThread_DATES extends PasswordCrackerThread {

	
	ArrayList<String> wordPerms = new ArrayList<>();
	ArrayList<String> acrPerms = new ArrayList<>();
	
	public PasswordCrackerThread_DATES(String nm, List<String> dict) {

		super(nm, dict);
	}

	public void run() {

		startTime = System.currentTimeMillis();

		// *** BASIC DICTIONARY WORD TESTS ***
		for (int i = 0; i < dictionary.size(); i++) {

			String dictionaryWord = dictionary.get(i);


			ArrayList<String> lauraPerm = WordPermuters.lauraPermuter(dictionaryWord);
			
			for(String lauraWord : lauraPerm){
				
				PasswordRunner.checkUsersForHash(lauraWord, startTime);
				
			}
		}
	}
}
