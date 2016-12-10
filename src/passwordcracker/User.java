package passwordcracker;

public class User {
	
	private static final MD5 MD5 = new MD5();
	
	private final String name;
	private final String salt;
	private final String hash;
	
	private boolean cracked;
	private String password;
	
	public User(String nm, String st, String hs){		
		name = nm;
		salt = st;
		hash = hs;		
		
		cracked = false;
		password = null;
	}
	
	
	public boolean compareHash(String cmp){
		
		// compares the hash of the user with the 
		// string compared concatenated with the salt
		String calc = MD5.hash(cmp + salt);
		
		if(hash.equals(calc)){
			cracked = true;
			password = cmp;
			return true;
		}		
		
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isCracked(){
		return cracked;
	}
	
	public String getSalt(){
		return salt;
	}
	
	public String getPassword(){
		if(cracked){
			return password;
		}
		
		return null;
	}
}
