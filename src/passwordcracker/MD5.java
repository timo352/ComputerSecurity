package passwordcracker;

import java.security.*;
import javax.xml.bind.*;

public class MD5 {

	private MessageDigest a = null;

	public MD5() {
		try {	
			a = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			System.exit(1);
		}		
	}

	public String hash(String str){		
		String out = DatatypeConverter.printHexBinary(a.digest(str.getBytes())).toLowerCase();
		return out;
	}
}
