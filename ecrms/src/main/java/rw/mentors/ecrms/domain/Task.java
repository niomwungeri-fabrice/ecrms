/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import rw.mentors.ecrms.dao.TaskDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 29, 2017
 */

@Entity
@Table(name = "TASK")
@NamedQueries({ @NamedQuery(name = TaskDao.QUERY_NAME.taskByMeeting, query = TaskDao.QUERY.taskByMeeting),
		@NamedQuery(name = TaskDao.QUERY_NAME.countPending, query = TaskDao.QUERY.countPending) })
public class Task extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME")
	private String name;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "GIVEN_DATE", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date givenDate;
	@Column(name = "START_DATE")
	private Date startDate;
	@Column(name = "DEADLINE")
	private Date deadLine;
	@Column(name = "STATUS")

	private String status;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "MEETING_UUID")

	private Meeting meeting;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ASSIGNED_BY")
	private Employee assignedTo;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ASSIGNED_TO")
	private Employee assignedBy;

	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "task")
	private List<TaskReport> taskReports;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getGivenDate() {
		return givenDate;
	}

	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Employee getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Employee assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Employee getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(Employee assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
