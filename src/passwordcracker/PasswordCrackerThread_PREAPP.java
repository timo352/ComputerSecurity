package passwordcracker;

import java.util.ArrayList;
import java.util.List;

public class PasswordCrackerThread_PREAPP extends PasswordCrackerThread {

	public PasswordCrackerThread_PREAPP(String nm, List<String> dict) {

		super(nm, dict);
	}

	public void run() {

		startTime = System.currentTimeMillis();

		// *** APP/PRE PEND CHARS
		for (int i = 0; i < dictionary.size(); i++) {
			
			String dictionaryWord = dictionary.get(i);
			ArrayList<String> appPerm = WordPermuters.appendCharacterPermuter(dictionaryWord, 1);
			
		
			for(String appWord : appPerm){
				
				ArrayList<String> prePerm = WordPermuters.prependCharacterPermuter(appWord, 1);
				
				for(String preWord : prePerm){
					PasswordRunner.checkUsersForHash(preWord, startTime);
				}				
			}
		}
	}
}
