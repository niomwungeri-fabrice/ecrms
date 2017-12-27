
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

import rw.mentors.ecrms.dao.MeetingDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "MEETING")
@NamedQueries({ @NamedQuery(name = MeetingDao.QUERY_NAME.findById, query = MeetingDao.QUERY.findById),
		@NamedQuery(name = MeetingDao.QUERY_NAME.pastMeetings, query = MeetingDao.QUERY.pastMeetings),
		@NamedQuery(name = MeetingDao.QUERY_NAME.upComingsMeetings, query = MeetingDao.QUERY.upComingsMeetings),
		@NamedQuery(name = MeetingDao.QUERY_NAME.todayMeeting, query = MeetingDao.QUERY.todayMeeting) })
public class Meeting extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "TITLE", nullable = false)
	private String title;
	@Column(name = "PURPOSE")
	private String purpose;
	@Column(name = "LOCATION")
	private String location;
	@Column(name = "START_TIME")
	private Date startTime;
	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "CREATED_ON", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdOn;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "meeting")
	private List<Task> tasks;

	@OneToMany(mappedBy = "meeting")
	private List<Topic> topicInMeeting;

	@OneToMany(mappedBy = "meeting")
	private List<ExpenditureItem> expItems;

	@OneToMany(mappedBy = "meeting")
	private List<Organization> organization;
	@OneToMany(mappedBy = "meeting")
	private List<Invitation> invitation;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "MEETING_TYPE_UUID")
	private MeetingType meetingType;

	@ManyToOne
	@JoinColumn(name = "CREATED_BY_UUID", nullable = false)
	private Employee employee;

	public MeetingType getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
	}

	public List<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Topic> getTopicInMeeting() {
		return topicInMeeting;
	}

	public void setTopicInMeeting(List<Topic> topicInMeeting) {
		this.topicInMeeting = topicInMeeting;
	}

	public List<ExpenditureItem> getExpItems() {
		return expItems;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setExpItems(List<ExpenditureItem> expItems) {
		this.expItems = expItems;
	}

	public List<Invitation> getInvitation() {
		return invitation;
	}

	public void setInvitation(List<Invitation> invitation) {
		this.invitation = invitation;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
