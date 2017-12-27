/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.Date;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.EmployeeDao;
import rw.mentors.ecrms.dao.InstitutionDao;
import rw.mentors.ecrms.dao.InvitationDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.dao.MeetingTypeDao;
import rw.mentors.ecrms.dao.OrganizationDao;
import rw.mentors.ecrms.dao.PresentationDao;
import rw.mentors.ecrms.dao.PresenterDao;
import rw.mentors.ecrms.dao.ResourceDocumentDao;
import rw.mentors.ecrms.dao.TaskDao;
import rw.mentors.ecrms.dao.TopicDao;
import rw.mentors.ecrms.domain.EExecutionStatus;
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
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 9, 2017
 */
@Service
public class CreatingMeetingServiceImpl extends TransactionAware implements CreatingMeetingService {

	@Autowired
	private ResourceDocumentDao rdDao;

	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private MeetingTypeDao mTypeDao;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private InvitationDao invDao;

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private InstitutionDao instaDao;

	@Autowired
	private PresentationDao presDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private PresenterDao presenterDao;

	@Override
	public String createMeeting(Meeting meeting, String organizerEmail) {
		try {
			Employee em = empDao.findByEmail(organizerEmail);
			Meeting m = meetingDao.save(meeting);
			Organization or = new Organization();
			or.setEmployee(em);
			or.setMeeting(m);
			or.setRole("Organizer");
			organizationDao.save(or);
			return "Meeting " + m.getTitle() + " Created.";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Meeting not created!, Contact System admin");
		}
	}

	@Override
	public String editMeetingInfor(Meeting meeting, String organizerEmail) {
		try {
			if (organizerEmail != "") {
				Employee em = empDao.findByEmail(organizerEmail);
				Meeting m = meetingDao.update(meeting);
				Organization or = organizationDao.organizerPerMeeting(meeting.getId());
				or.setEmployee(em);
				or.setMeeting(m);
				organizationDao.update(or);
				return "Meeting Info Edited Successfully";
			} else {
				meetingDao.update(meeting);
				return "Meeting Info Edited Successfully";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<MeetingType> findAllMeetingType() {
		return mTypeDao.findAll();
	}

	@Override
	public List<Meeting> findAllMeetings() {
		return meetingDao.findAll();
	}

	@Override
	public Meeting findMeetingById(String mId) {
		return meetingDao.findById(mId);
	}

	@Override
	public MeetingType findByMtType(String mtId) {
		return mTypeDao.findById(mtId);
	}

	@Override
	public String addTopic(Topic topic) {
		try {
			topicDao.save(topic);
			return "Topic " + topic.getName() + " added.";
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException("Conflict Time with topics, check Time and try again");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Topic not created!, Contact System admin");
		}
	}

	@Override
	public String editTopic(Topic topic) {
		try {
			topicDao.update(topic);
			return "Topic " + topic.getName() + " Updated.";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Topic not updated!, Contact System admin>>" + e.getMessage());
		}
	}

	@Override
	public List<Topic> topicsByMeetingList(String meeting) {
		return topicDao.topicsByMeeting(meeting);
	}

	@Override
	public String removeTopic(Topic topic) {
		try {
			Topic t = topicDao.delete(topic);
			return "Topic " + t.getName() + " Removed.";
		} catch (Exception e) {
			throw new DataManipulationException("Topic not Removed!, Contact System admin>>" + e.getMessage());
		}

	}

	@Override
	public List<Topic> allTopic() {
		return topicDao.findAll();
	}

	// Institution
	@Override
	public Institution findByEmail(String email) {
		try {
			return instaDao.findByEmail(email);
		} catch (Exception e) {
			throw new DataManipulationException("Institution not found" + e.getMessage());
		}
	}

	@Override
	public Employee finByEmail(String email) {
		try {
			return empDao.findByEmail(email);
		} catch (Exception e) {
			throw new DataManipulationException("Employee not found" + e.getMessage());
		}
	}

	@Override
	public void createPresentation(Presentation presentation) {
		try {
			presDao.save(presentation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Meeting not Updated!,Contact system admin");
		}
	}

	@Override
	public List<Institution> findAllInstutions() {
		return instaDao.findAll();
	}

	@Override
	public List<Employee> findAllEmployees() {
		return empDao.findAll();
	}

	@Override
	public String invite(Invitation invitation) {
		try {
			invDao.save(invitation);
			return "Invation Sent Successfully";
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException("Some participants already invited");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Invitations Not Sent" + e.getMessage());
		}
	}

	@Override
	public List<Invitation> invitationByMeeting(String meetingId) {
		return invDao.invitationByMeeting(meetingId);
	}

	@Override
	public List<Invitation> assigness(String email) {
		return invDao.assignees(email);
	}

	@Override
	public List<Task> taskByMeeting(String meetingId) {
		try {
			return taskDao.taskByMeeting(meetingId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Meeting> upComingsMeetings(Date currentDate) {
		try {
			return meetingDao.upComingMeetings(currentDate);
		} catch (Exception e) {

			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public ResourceDocument fileSaver(ResourceDocument resourceDocument) {
		try {
			return rdDao.save(resourceDocument);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String createTask(Task task, String by, String to, String meetingId, ResourceDocument document) {
		try {
			Employee assigned = empDao.findByEmail(by);
			Employee assignee = empDao.findByEmail(to);
			Meeting meeting = meetingDao.findById(meetingId);
			task.setAssignedBy(assigned);
			task.setAssignedTo(assignee);
			task.setMeeting(meeting);
			task.setStatus(EExecutionStatus.PENDING.toString());
			if (document.getFileName() == null) {
				taskDao.save(task);
				return "Task Created Successfully";
			}
			Task t = taskDao.save(task);
			document.setTask(t);
			rdDao.update(document);
			return "Task Created Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public Organization organizerPerMeeting(String meetingId) {
		try {
			return organizationDao.organizerPerMeeting(meetingId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public void updatePresentation(Presentation presentation) {
		presDao.update(presentation);

	}

	@Override
	public Presenter presenterById(String id) {
		return presenterDao.presenterById(id);
	}

	@Override
	public Presentation presentationByPresenter(String presenterId) {
		return presDao.presentationByPresenter(presenterId);
	}

}
