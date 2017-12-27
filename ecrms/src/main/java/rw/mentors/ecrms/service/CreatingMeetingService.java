/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.Date;
import java.util.List;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.MeetingType;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Presentation;
import rw.mentors.ecrms.domain.Presenter;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.Topic;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 9, 2017
 */
public interface CreatingMeetingService {

	// meeting
	String createMeeting(Meeting meeting, String organizerEmail);

	List<MeetingType> findAllMeetingType();

	List<Meeting> findAllMeetings();

	Meeting findMeetingById(String mId);

	MeetingType findByMtType(String mtId);

	String editMeetingInfor(Meeting meeting, String organizerEmail);

	List<Meeting> upComingsMeetings(Date currentDate);

	// topic
	String addTopic(Topic topic);

	String editTopic(Topic topic);

	List<Topic> topicsByMeetingList(String meeting);

	String removeTopic(Topic topic);

	List<Topic> allTopic();

	// Invitation
	String invite(Invitation invitation);

	List<Invitation> invitationByMeeting(String meetingId);

	List<Invitation> assigness(String email);

	// Institution
	Institution findByEmail(String email);

	List<Institution> findAllInstutions();

	// employee
	Employee finByEmail(String email);

	List<Employee> findAllEmployees();

	// presentation
	void createPresentation(Presentation presentation);

	void updatePresentation(Presentation presentation);

	Presentation presentationByPresenter(String presenterId);

	// Presenter
	Presenter presenterById(String id);

	// tasks
	String createTask(Task task, String by, String to, String meeting, ResourceDocument document);

	List<Task> taskByMeeting(String meetingId);

	ResourceDocument fileSaver(ResourceDocument resourceDocument);

	// Organizer

	Organization organizerPerMeeting(String meetingId);
}
