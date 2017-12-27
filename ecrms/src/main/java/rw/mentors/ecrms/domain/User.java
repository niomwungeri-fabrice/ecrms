/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import rw.mentors.ecrms.dao.UserDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "USER")
@NamedQueries({ @NamedQuery(name = UserDao.QUERY_NAME.findById, query = UserDao.QUERY.findById),
		@NamedQuery(name = UserDao.QUERY_NAME.findByUsername, query = UserDao.QUERY.findByUsername) })
public class User extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "USERNAME", nullable = false, unique = true)
	private String username;
	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "PROFILE_IMAGE")
	@Lob
	private byte[] profileImage;

	@JoinColumn(name = "EMPLOYEE_UUID")
	@OneToOne(cascade = CascadeType.MERGE)
	private Employee employee;

	@JoinColumn(name = "ROLE_UUID")
	@ManyToOne(cascade = CascadeType.MERGE)
	private Role roles;

	@OneToMany(mappedBy = "user")
	private List<UserRoles> userRoles;

	public Role getRoles() {
		return roles;
	}

	public void setRoles(Role roles) {
		this.roles = roles;
	}

	public List<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

}
