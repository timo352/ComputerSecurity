/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordcracker;

import java.util.List;

/**
 *
 * @author Timothy
 */
public class PasswordCrackerThread implements Runnable{

	protected String name;
	protected List<String> dictionary;
	protected long startTime;
	
	public PasswordCrackerThread(String nm, List<String> dict) {

		name = nm;
		dictionary = dict;
		
		System.out.println(name + " created.");
	}
	
	
	
	public void run() {
		
		return;
		
	}
	
}
