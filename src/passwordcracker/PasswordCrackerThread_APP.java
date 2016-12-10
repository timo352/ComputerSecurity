package passwordcracker;

import java.util.ArrayList;
import java.util.List;

public class PasswordCrackerThread_APP extends PasswordCrackerThread {

	public PasswordCrackerThread_APP(String nm, List<String> dict) {

		super(nm, dict);
	}

	public void run() {

		startTime = System.currentTimeMillis();

		// *** APPENDING CHARS ***
		for (int i = 0; i < dictionary.size(); i++) {
			
			String dictionaryWord = dictionary.get(i);
			ArrayList<String> appPerm = WordPermuters.appendCharacterPermuter(dictionaryWord, 2);

			for (String appWord : appPerm) {
				PasswordRunner.checkUsersForHash(appWord, startTime);
			}
		}
	}
}
