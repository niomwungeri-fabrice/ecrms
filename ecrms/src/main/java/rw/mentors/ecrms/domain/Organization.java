package rw.mentors.ecrms.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import rw.mentors.ecrms.dao.OrganizationDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "ORGANIZATION", uniqueConstraints = @UniqueConstraint(columnNames = { "EMPLOYEE_UUID", "MEETING_UUID" }))
@NamedQueries({
		@NamedQuery(name = OrganizationDao.QUERY_NAME.organizerPerMeeting, query = OrganizationDao.QUERY.organizerPerMeeting) })
public class Organization extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "ROLE", nullable = false)
	private String role;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "MEETING_UUID", nullable = false)
	private Meeting meeting;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "EMPLOYEE_UUID", nullable = false)
	private Employee employee;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
