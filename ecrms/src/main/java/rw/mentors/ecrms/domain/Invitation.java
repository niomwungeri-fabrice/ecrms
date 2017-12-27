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

import rw.mentors.ecrms.dao.InvitationDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity

@Table(name = "INVITATION", uniqueConstraints = { @UniqueConstraint(columnNames = { "EMPLOYEE_UUID", "MEETING_UUID" }),
		@UniqueConstraint(columnNames = { "INSTITUTION_UUID", "MEETING_UUID" }) })
@NamedQueries({
		@NamedQuery(name = InvitationDao.QUERY_NAME.invitationByMeeting, query = InvitationDao.QUERY.invitationByMeeting),
		@NamedQuery(name = InvitationDao.QUERY_NAME.assignees, query = InvitationDao.QUERY.assignees) })
public class Invitation extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "ATTENDED")
	private boolean attended;
	@Column(name = "CONFIRMED")
	private boolean confirmed;
	@Column(name = "REASON")
	private String reason;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MEETING_UUID", nullable = false)
	private Meeting meeting;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "EMPLOYEE_UUID", nullable = true)
	private Employee employee;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "INSTITUTION_UUID", nullable = true)
	private Institution institution;

	@Column(name = "DELEGATED")
	private Boolean delegated;

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

	public boolean isAttended() {
		return attended;
	}

	public void setAttended(boolean attended) {
		this.attended = attended;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public Boolean getDelegated() {
		return delegated;
	}

	public void setDelegated(Boolean delegated) {
		this.delegated = delegated;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
