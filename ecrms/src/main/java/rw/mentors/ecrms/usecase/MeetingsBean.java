/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.User;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.MeetingReviewService;
import rw.mentors.ecrms.service.TaskReportingService;
import rw.mentors.ecrms.service.UserService;

/**
 * @author NIYOMWUNGERI
 * @Date Oct 16, 2017
 */
@Component
@ManagedBean(name = "meetingsBean")
@SessionScoped
public class MeetingsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MeetingReviewService meetingReviewService;

	@Autowired
	private CreatingMeetingService cmService;

	@Autowired
	private MeetingReviewService msService;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskReportingService reportingService;

	private List<Meeting> filteredMeetings;

	private Meeting selectedMeeting;

	private String username;

	private boolean disableEdit;

	public String getMeetingObject(Meeting meeting) {
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		this.selectedMeeting = meeting;
		meetingId = selectedMeeting.getId();
		return "meetingDetails";
	}

	public int pendingExecutions() {
		int countPendingExecution = 0;
		for (Execution execution : executionAssignedToMe()) {
			if (execution.getStatus().equals(EExecutionStatus.PENDING.toString())) {
				countPendingExecution++;
			}
		}
		return countPendingExecution;
	}

	public List<Task> taskByMeeting() {
		return cmService.taskByMeeting(meetingId);
	}

	public List<Task> assignedToMe() {
		List<Task> tB = taskByMeeting();
		List<Task> assignedTaskList = new ArrayList<Task>();
		for (Task t : tB) {
			if (t.getAssignedTo().getEmail().equals(username)) {
				assignedTaskList.add(t);
			}
		}
		return assignedTaskList;
	}

	public int pendingTasks() {
		int countPendingTasks = 0;
		for (Task task : assignedToMe()) {
			if (task.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())) {
				countPendingTasks++;
			}
		}
		return countPendingTasks;
	}

	private String meetingId;

	public List<Execution> executionAssignedToMe() {
		List<Execution> execu = reportingService.executionByResolution(meetingId);
		List<Execution> execuNew = new ArrayList<Execution>();
		for (Execution e : execu) {
			if (e.getAssignedTo().getEmail().equals(this.username)) {
				execuNew.add(e);
			}
		}
		return execuNew;
	}

	public List<Meeting> myMeetings() {
		User user = userService.usernameByEmail(username);

		List<Meeting> testList = new ArrayList<Meeting>();
		if (user.getRoles().getName().equalsIgnoreCase("organizer")) {
			for (Meeting meeting : meetingReviewService.allMeetings()) {
				if (meeting.getEmployee().getEmail().equals(this.username)) {
					testList.add(meeting);
				}
			}
			testList.sort(Comparator.comparing(Meeting::getCreatedOn).reversed());
			disableEdit = false;
			return testList;
		} else {
			disableEdit = true;
			testList = msService.myMeetings(username);
			testList.sort(Comparator.comparing(Meeting::getCreatedOn).reversed());
			return testList;
		}
	}

	public List<Meeting> meetings() {
		return meetingReviewService.allMeetings();
	}

	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a - d MMM yy");
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

	// get meeting year
	public String meetingYear(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("yyyy");
		return outputFormat.format(date);
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	private Organization organizer;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Organization orginier() {
		return this.organizer;
	}

	public List<Meeting> getFilteredMeetings() {
		return filteredMeetings;
	}

	public void setFilteredMeetings(List<Meeting> filteredMeetings) {
		this.filteredMeetings = filteredMeetings;
	}

	public Organization getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Organization organizer) {
		this.organizer = organizer;
	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public boolean isDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

}
