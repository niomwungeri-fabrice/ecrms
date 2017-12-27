/**
 * 
 */
package rw.mentors.ecrms.test;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 21, 2017
 */
public class ConsoleTesting2 {
	
	public static void main(String[] args) {
		String password = "123";
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
		System.out.println(hashed); 
	}
}
