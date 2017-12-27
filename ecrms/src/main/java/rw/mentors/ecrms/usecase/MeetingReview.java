/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.service.AttendanceService;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.MeetingReviewService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 19, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class MeetingReview implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MeetingReviewService msService;

	@Autowired
	private CreatingMeetingService cmService;

	@Autowired
	private AttendanceService attendanceService;

	private Meeting selectedMeeting;

	private boolean disableMeetingMinutes = false;

	private Organization organizer;
	public String username;
	private String searchKey;
	private List<Meeting> meetings;
	private Employee employee = new Employee();
	private Date today = new Date();
	private Date fromDate;
	private Date toDate;
	private String meetingId;
	private String reason;

	public void confirmEmployeeAttendance() {
		try {
			String msg = "";
			for (Invitation invitation : attendanceService.findInvited(meetingId)) {
				if (invitation.getEmployee() != null) {
					if (invitation.getEmployee().getEmail().equals(username)) {
						invitation.setConfirmed(true);
						msg = attendanceService.updateInvitation(invitation);
						successMessages("Success", msg);
					} else {
						successMessages("Success", "else");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error", e.getMessage());
		}
	}

	public void cancelEmployeeAttendance() {
		try {
			String msg = "";
			for (Invitation invitation : attendanceService.findInvited(meetingId)) {
				if (invitation.getEmployee() != null) {
					if (invitation.getEmployee().getEmail().equals(username)) {
						invitation.setConfirmed(false);
						invitation.setReason(reason);
						msg = attendanceService.updateInvitation(invitation);
						successMessages("Success", msg);
						reason = new String();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error", e.getMessage());
		}

	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	// formated Date
	public String meetingTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
		return outputFormat.format(date);
	}

	public String getMeetingObject(Meeting meeting) {
		this.selectedMeeting = meeting;
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		this.meetingId = selectedMeeting.getId();
		return "meetingreview";
	}

	public void getMeetingObj(Meeting meeting) {
		this.selectedMeeting = meeting;
		meetingId = meeting.getId();
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
	}

	public MeetingReview() {
		meetings = new ArrayList<Meeting>();
	}

	public List<EExecutionStatus> eExecutionStatus() {
		return EExecutionStatus.getExecutionStatus();
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
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

	public void setToday(Date today) {
		this.today = today;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public void searchByDate() throws ParseException {
		meetings = new ArrayList<Meeting>();
		for (Meeting meeting : msService.allMeetings()) {
			if (meeting.getStartTime().compareTo(fromDate) >= 0 && meeting.getStartTime().compareTo(toDate) <= 0) {
				meetings.add(meeting);
				System.err.println("Test:" + meeting.getStartTime());
			}
		}
		if (meetings.size() > 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Search Results:",
							(meetings.size() == 1) ? "" + meetings.size() + " Record is Found"
									: "" + meetings.size() + " Records are Found"));
		} else if (meetings.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Result not found:", " [ " + searchKey + " ] No matching Record!"));
		} else {
			this.meetings = new ArrayList<Meeting>();
		}
	}

	public void searchListener() {
		for (Meeting meeting : msService.allMeetings()) {
			if (meeting.getTitle().contains(searchKey)) {
				meetings.add(meeting);
				searchKey = "";
				break;
			} else {
				this.meetings = new ArrayList<Meeting>();
			}
		}
		if (meetings.size() > 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Search Results:",
							(meetings.size() == 1) ? "" + meetings.size() + " Record is Found"
									: "" + meetings.size() + " Records are Found"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Result not found:", " [ " + searchKey + " ] No matching Record!"));
		}
	}

	public List<Resolution> getResolutions(Topic topic) {
		return msService.resolutionByTopic(topic.getId());
	}

	public List<Execution> getResoTasks(Topic topic) {
		List<Execution> res = new ArrayList<Execution>();
		for (Resolution r : msService.resolutionByTopic(topic.getId())) {
			for (Execution ex : msService.executionPerResolution(r.getId())) {
				res.add(ex);
			}
		}
		return res;
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	public String convertToTimeReport(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy");
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

	public List<Resolution> resolutionsByTopic(Topic topic) {
		return msService.resolutionByTopic(topic.getId());
	}

	public List<Topic> topicNonExecution() {
		return msService.topicByMeeting(meetingId);
	}

	public List<Topic> topicByMeetingRecommendation() {
		List<Topic> t = new ArrayList<Topic>();
		for (Topic topi : msService.topicByMeeting(meetingId)) {
			for (Resolution r : msService.resolutionByTopic(topi.getId())) {
				for (Execution ex : msService.executionPerResolution(r.getId())) {
					if (r.getId().equals(ex.getResolution().getId())) {
						if (!t.contains(topi)) {
							t.add(topi);
						}
					}
				}
			}
		}
		return t;
	}

	public List<Invitation> attendeesPerMeeting() {
		List<Invitation> invitations = new ArrayList<Invitation>();
		for (Invitation invitation : msService.attendees(meetingId)) {
			if (invitation.getInstitution() != null) {
				invitations.add(invitation);
			}
		}
		return invitations;
	}

	public List<Invitation> attendeesPerMeetingEm() {
		List<Invitation> invitations = new ArrayList<Invitation>();
		for (Invitation invitation : msService.attendees(meetingId)) {
			if (invitation.getEmployee() != null) {
				invitations.add(invitation);
			}
		}
		return invitations;
	}

	public List<Meeting> findAll() {
		return msService.allMeetings();
	}

	public void viewMeeting() {
		employee = msService.findById(username);
		this.disableMeetingMinutes = true;

	}

	public List<Meeting> past() {
		List<Meeting> pMeeting = new ArrayList<Meeting>();
		for (Meeting m : myMeetings()) {
			if (m.getStartTime().before(new Date())) {
				pMeeting.add(m);
			}
		}
		return pMeeting;
	}

	public List<Meeting> future() {
		List<Meeting> fMeeting = new ArrayList<Meeting>();
		for (Meeting m : myMeetings()) {
			if (m.getStartTime().after(new Date())) {
				fMeeting.add(m);
			}
		}
		return fMeeting;
	}

	public void setOrganizer(Organization organizer) {
		this.organizer = organizer;
	}

	public Organization getOrganizer() {
		return organizer;
	}

	public String customeDate(Meeting meeting) {
		organizer = msService.organizerPerMeeting(meetingId);
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
		Date date;
		if (meeting.getStartTime() == null) {
			date = new Date();
			return outputFormat.format(date);
		}
		return outputFormat.format(meeting.getStartTime());
	}

	public List<Meeting> myMeetings() {
		return msService.myMeetings(username);
	}

	public Meeting getSelectedMeeting() {

		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public boolean isDisableMeetingMinutes() {
		return disableMeetingMinutes;
	}

	public void setDisableMeetingMinutes(boolean disableMeetingMinutes) {
		this.disableMeetingMinutes = disableMeetingMinutes;
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
