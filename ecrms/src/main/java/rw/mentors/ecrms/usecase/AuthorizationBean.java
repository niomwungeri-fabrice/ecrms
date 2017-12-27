/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Role;
import rw.mentors.ecrms.domain.User;
import rw.mentors.ecrms.service.SecurityService;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 23, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class AuthorizationBean {
	private Date today = new Date();

	@Autowired
	private SecurityService securityService;

	private Meeting test;
	private String username;
	private Role role = new Role();
	private DualListModel<Role> roles;
	List<Role> allRoles = new ArrayList<Role>();
	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void deleteUserEmployee(User user) {
		try {
			String msg = securityService.deleteUser(user);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}

	}

	public void passWordReset(User user) {
		try {
			user.setPassword(md5("123"));
			String msg = securityService.passwordReset(user);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public String md5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte bytData[] = md.digest();
			StringBuffer hextString = new StringBuffer();
			for (int i = 0; i < bytData.length; i++) {
				String hex = Integer.toHexString(0xff & bytData[i]);
				if (hex.length() == 1) {
					hextString.append('0');
				}
				hextString.append(hex);
			}
			return hextString.toString();

		} catch (Exception e) {
			return "Error" + e.getMessage();
		}

	}

	public void onRowEdit(RowEditEvent event) {
		try {
			User user = (User) event.getObject();
			user.getRoles().setId(roleId);
			String msg = securityService.changeUserAccess(user);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	// format dates
	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public DualListModel<Role> getRoles() {
		return roles;
	}

	public void setRoles(DualListModel<Role> roles) {
		this.roles = roles;
	}

	public List<Role> allRoles() {
		return securityService.allRoles();
	}

	public void getRoleObject(Role role) {
		this.role = role;
	}

	public void getUserOBject(User u) {
		this.user = u;
	}

	public void createRole() {
		try {
			String msg = securityService.createRole(role);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void deleteRole(Role role) {
		try {
			String msg = securityService.deleteRole(role);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void updateRole(Role role) {
		try {
			String msg = securityService.updateRole(role);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	@PostConstruct
	public void init() {

		allRoles = securityService.allRoles();
		// Picklist
		List<Role> availableRoles = new ArrayList<Role>();
		List<Role> assignedRoles = new ArrayList<Role>();

		for (Role role : securityService.allRoles()) {
			availableRoles.add(role);
		}

		roles = new DualListModel<Role>(availableRoles, assignedRoles);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void activateUser(User user) {
		try {
			String msg = securityService.activateUser(user);
			successMessages("Success", msg);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error:", e.getMessage());
		}
	}

	public void assignRoles() {
		try {
			String msg = "";
			for (Role role : roles.getTarget()) {
				msg = securityService.assignRoles(role, user.getId());
			}
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public List<User> allUser() {
		return securityService.allUsers();
	}

	private User user = new User();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getToday() {
		return today;
	}

	public Meeting getTest() {
		return test;
	}

	public void setTest(Meeting test) {
		this.test = test;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public void successMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void warningMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void errorMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
