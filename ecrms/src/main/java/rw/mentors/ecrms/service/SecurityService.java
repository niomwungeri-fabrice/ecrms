/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.Role;
import rw.mentors.ecrms.domain.User;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 19, 2017
 */
public interface SecurityService {

	// user
	List<User> allUsers();

	String activateUser(User user);

	String passwordReset(User user);

	String changeUserAccess(User user);
	
	String deleteUser(User user);

	// roles
	List<Role> allRoles();

	String createRole(Role role);

	String updateRole(Role role);

	String deleteRole(Role id);

	String assignRoles(Role role, String userId);


}
