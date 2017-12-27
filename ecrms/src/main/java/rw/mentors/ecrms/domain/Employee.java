package rw.mentors.ecrms.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import rw.mentors.ecrms.dao.EmployeeDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 6, 2017
 */
@Entity
@Table(name = "EMPLOYEE")
@NamedQueries({ @NamedQuery(name = EmployeeDao.QUERY_NAME.findByEmail, query = EmployeeDao.QUERY.findByEmail) })
public class Employee extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "GENDER")
	private String gender;
	@Column(name = "POSITION")
	private String position;

	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;
	@OneToOne(mappedBy = "employee")
	private User user;

	@OneToMany(mappedBy = "employee")
	private List<ExecutationReport> executionReports;

	@OneToMany(mappedBy = "assignedTo")
	private List<Task> tasksAssignedTo;

	@OneToMany(mappedBy = "employee")
	private List<Organization> organization;

	@OneToMany(mappedBy = "employee")
	private List<Invitation> invitation;

	@OneToMany(mappedBy = "assignedTo")
	private List<Execution> executionTo;
	@OneToMany(mappedBy = "assignedBy")
	private List<Execution> executionBy;

	@OneToMany(mappedBy = "employee")
	private List<Presenter> presenters;
	
	@OneToMany(mappedBy = "employee")
	private List<Meeting> meetings;

	public List<Presenter> getPresenters() {
		return presenters;
	}

	public void setPresenters(List<Presenter> presenters) {
		this.presenters = presenters;
	}

	public List<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		firstName = firstName.toUpperCase();
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<Invitation> getInvitation() {
		return invitation;
	}

	public void setInvitation(List<Invitation> invitation) {
		this.invitation = invitation;
	}

	public List<Task> getTasksAssignedTo() {
		return tasksAssignedTo;
	}

	public void setTasksAssignedTo(List<Task> tasksAssignedTo) {
		this.tasksAssignedTo = tasksAssignedTo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Execution> getExecutionTo() {
		return executionTo;
	}

	public void setExecutionTo(List<Execution> executionTo) {
		this.executionTo = executionTo;
	}

	public List<Execution> getExecutionBy() {
		return executionBy;
	}

	public void setExecutionBy(List<Execution> executionBy) {
		this.executionBy = executionBy;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ExecutationReport> getExecutionReports() {
		return executionReports;
	}

	public void setExecutionReports(List<ExecutationReport> executionReports) {
		this.executionReports = executionReports;
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

}
