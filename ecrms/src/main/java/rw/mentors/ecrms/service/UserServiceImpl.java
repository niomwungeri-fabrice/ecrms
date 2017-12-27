/**
 * 
 */
package rw.mentors.ecrms.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.UserDao;
import rw.mentors.ecrms.domain.User;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 15, 2017
 */
@Service
public class UserServiceImpl extends TransactionAware implements UserService {
	@Autowired
	private UserDao userDao;
	@Override
	public String createUser(User user) {
		try {
			userDao.save(user);
			return "Account created successfully";
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException("Account already exist!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Account not found, Contact administrator");
		}
	}

	@Override
	public String forgotPassord(String email,String secCode) {
		try {
			String msg = "";
			User user = userDao.findByUsername(email);
			if (user != null) {
				user.setPassword(secCode);
				userDao.update(user);
				msg = "Password resetted successfully ";
			}
			return msg;

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("User doesn't exist");
		}
	}

	@Override
	public String login(User u) {
		try {
			User user = userDao.findByUsername(u.getUsername());
			if (user.isState()) {
				if (user.getPassword().equals(u.getPassword())) {
					if (user.getRoles().getName().equalsIgnoreCase("admin")) {
						return "security";
					} else if (user.getRoles().getName().equalsIgnoreCase("organizer")) {
						return "meeting";
					} else if (user.getRoles().getName().equalsIgnoreCase("normal")) {
						return "dashboard";
					} else {
						return "No access level given";
					}
				} else {
					return "Username and password don't match";
				}
			} else {
				return "Access denied , Contact system administrator";
			}
		} catch (Exception e) {
			throw new DataManipulationException("User not found");
		}
	}

	@Override
	public User usernameByEmail(String email) {
		return userDao.findByUsername(email);
	}

	@Override
	public String editProfile(User user) {
		try {
			userDao.update(user);
			return "User updated successfully";
		} catch (Exception e) {
			throw new DataManipulationException("User not updated,error:" + e.getMessage());
		}
	}

	@Override
	public User updateImage(User user) {
		try {
			return userDao.update(user);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	
}
