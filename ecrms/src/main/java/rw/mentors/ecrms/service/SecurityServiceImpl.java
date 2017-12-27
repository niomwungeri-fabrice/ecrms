/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.RoleDao;
import rw.mentors.ecrms.dao.UserDao;
import rw.mentors.ecrms.dao.UserRoleDao;
import rw.mentors.ecrms.domain.Role;
import rw.mentors.ecrms.domain.User;
import rw.mentors.ecrms.domain.UserRoles;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 19, 2017
 */
@Service
public class SecurityServiceImpl extends TransactionAware implements SecurityService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public List<User> allUsers() {
		return userDao.findAll();
	}

	@Override
	public String activateUser(User user) {
		try {
			if (user.isState()) {
				user.setState(false);
				userDao.update(user);
				return "User Disabled";
			} else {
				user.setState(true);
				userDao.update(user);
				return "User unabled";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Role> allRoles() {
		return roleDao.findAll();
	}

	@Override
	public String createRole(Role role) {
		try {
			roleDao.save(role);
			return "Role Added Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String updateRole(Role role) {
		try {
			roleDao.update(role);
			return "Roled updated Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String deleteRole(Role role) {
		try {
			roleDao.delete(role);
			return "Roled Deleted Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String assignRoles(Role role, String userId) {
		try {
			User user = userDao.findById(userId);
			UserRoles uRoles = new UserRoles();
			uRoles.setUser(user);
			uRoles.setRole(role);
			userRoleDao.save(uRoles);
			return "Role(s) Assigned Successfully";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String passwordReset(User user) {
		try {
			userDao.update(user);
			return "Password Reset Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String changeUserAccess(User user) {
		try {
			userDao.saveOrUpdate(user);
			return "New Role Given Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String deleteUser(User user) {
		try {
			userDao.delete(user);
			return "User deleted successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

}
