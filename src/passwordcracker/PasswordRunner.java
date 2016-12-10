package passwordcracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class PasswordRunner {

	private static final MD5 MD5 = new MD5();

	private static final List<User> users = new ArrayList<>(1);

	private static final Map<String, String> hashLeak = Collections.synchronizedMap(new HashMap<String, String>());

	private static final List<String> saltLeak = Collections.synchronizedList(new ArrayList<>());
	private static final List<String> uniqueSaltLeak = Collections.synchronizedList(new ArrayList<>());

	private static final List<String> dictionary = new ArrayList<>();

	private static Thread t1;
	private static Thread t2;
	private static Thread t3;
	private static Thread t4;
	private static Thread t5;
	private static Thread t6;

	private static long count = 0;
	private static long numCracked = 0;

	private static PrintWriter out = null;

	public PasswordRunner() {

		Scanner userIn = null, dictIn = null;

		try {
			System.out.print("Loading files...");
			userIn = new Scanner(new File("userLeak.txt"));
			dictIn = new Scanner(new File("bible.txt"));
			System.out.println("Files loaded.");
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot find a file needed.");
			System.exit(1);
		}

		// read in the user leak file
		System.out.print("Reading in hashes and users...");
		while (userIn.hasNextLine()) {
			String[] prts = userIn.nextLine().split(":");

			users.add(new User(prts[0], prts[1], prts[2]));

			saltLeak.add(prts[1]);

			// read in the salts for iterating
			if (!uniqueSaltLeak.contains(prts[1])) {
				uniqueSaltLeak.add(prts[1]);
			}

			hashLeak.put(prts[2], prts[2]);
		}
		System.out.println("Hashes and users loaded.");

		// create the dictionary of Bible words
		// do not repeat words and remove all non-words
		System.out.print("Reading in dictionary...");
		dictIn.useDelimiter("[^A-Za-z0-9]");
		while (dictIn.hasNext()) {

			while (dictIn.hasNextInt()) {
				dictIn.next();
			}
			String str = dictIn.next().toLowerCase();

			if (!dictionary.contains(str) && !str.isEmpty()) {
				dictionary.add(str);
				dictionary.add(Character.toUpperCase(str.charAt(0)) + str.substring(1));
			}
		}
		System.out.println("Dictionary loaded.");

		try {
			out = new PrintWriter("crackedPasswords.txt", "UTF-8");

		} catch (FileNotFoundException ex) {
			System.out.println("OOPS");
			System.exit(1);
		} catch (UnsupportedEncodingException ex) {
			System.out.println("OOPS");
			System.exit(1);
		}

		// CREATE ALL THE THREADS
		System.out.println("Creating threads...");
		PasswordCrackerThread pct1 = new PasswordCrackerThread_BASIC("Basic Word Checking", dictionary);
		PasswordCrackerThread pct2 = new PasswordCrackerThread_APP("Appending Two Characters", dictionary);
		PasswordCrackerThread pct3 = new PasswordCrackerThread_DATES("Appending Common Dates", dictionary);
		PasswordCrackerThread pct4 = new PasswordCrackerThread_PREAPP("Appending/Prepending One Character", dictionary);
		
		t1 = new Thread(pct1);
		t2 = new Thread(pct2);
		t3 = new Thread(pct3);
		t4 = new Thread(pct4);
		//t5 = new Thread(pct5);
		//t6 = new Thread(pct6);
		System.out.println("All threads created.");
	}

	public void crack() {
		try {
			System.out.println(".\n.\n.\nCRACKING PASSWORDS...\n--------------------------------------------");

			t1.start();
			t2.start();
			t3.start();
			t4.start();
			//t5.start();
			//t6.start();

			t1.join();
			t2.join();
			t3.join();
			t4.join();
			//t5.join();
			//t6.join();			

			System.out.println("Checked " + count + " passwords.");
			System.out.println("Cracked " + numCracked + " passwords.");
			out.close();
		} catch (InterruptedException ex) {
			System.out.println("OOPS");
			System.exit(1);
		}
	}

	public static void checkUsersForHash(String word, long startTime) {

		synchronized (users) {

			List<String> foundSalts = Collections.synchronizedList(new ArrayList<>());
			List<User> foundUsers = Collections.synchronizedList(new ArrayList<>());

			// check all the strings with all possible salts
			String salt = null;
			for (int i = 0; i < uniqueSaltLeak.size(); i++) {

				salt = uniqueSaltLeak.get(i);

				// if the hash is found somewhere look for that user
				if (hashLeak.containsKey(MD5.hash(word + salt))) {

					saltLeak.remove(salt);
					foundSalts.add(salt);
					hashLeak.remove(MD5.hash(word + salt));

					User u = null;
					for (int j = 0; j < users.size(); j++) {

						u = users.get(j);

						if (u.compareHash(word)) {
							foundUsers.add(u);

							numCracked++;
							long currentTime = System.currentTimeMillis() - startTime;

							Date date = new Date(currentTime);
							DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");

							System.out.println(u.getName() + " : " + u.getPassword() + " : " + formatter.format(date));
							out.println(u.getName() + " : " + u.getPassword() + " : " + formatter.format(date));
						}
					}
					for (User foundUser : foundUsers) {
						users.remove(foundUser);
					}
				}
			}

			for (String foundSalt : foundSalts) {
				if (!saltLeak.contains(foundSalt)) {
					uniqueSaltLeak.remove(foundSalt);
				}
			}
			
			
			
		}

		++count;
		if (hashLeak.isEmpty() || users.isEmpty()) {
			System.out.println("FOUND ALL PASSWORDS");
			System.exit(0);
		}
	}

	public static void main(String[] args) {

		PasswordRunner p = new PasswordRunner();
		p.crack();
	}

}
