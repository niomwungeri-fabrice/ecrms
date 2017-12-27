/**
 * 
 */
package rw.mentors.ecrms.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import rw.mentors.ecrms.dao.UserRoleDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 19, 2017
 */
@Entity
@Table(name = "USER_ROLES", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_UUID", "ROLE_UUID" }))
@NamedQueries({ @NamedQuery(name = UserRoleDao.QUERY_NAME.userRoles, query = UserRoleDao.QUERY.userRoles) })
public class UserRoles extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@JoinColumn(name = "USER_UUID", nullable = false)
	@ManyToOne(cascade = CascadeType.MERGE)
	private User user;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ROLE_UUID", nullable = false)
	private Role role;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
