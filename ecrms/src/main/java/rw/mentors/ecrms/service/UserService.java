/**
 * 
 */
package rw.mentors.ecrms.service;

import rw.mentors.ecrms.domain.User;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 15, 2017
 */
public interface UserService {

	String createUser(User user);

	String forgotPassord(String email,String secCode);

	String login(User user);

	User usernameByEmail(String email);
	
	String editProfile(User user);
	
	User updateImage(User user);
}
