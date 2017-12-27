/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.EmployeeDao;
import rw.mentors.ecrms.dao.ExecutionDao;
import rw.mentors.ecrms.dao.InvitationDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.dao.OrganizationDao;
import rw.mentors.ecrms.dao.ResolutionDao;
import rw.mentors.ecrms.dao.TopicDao;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 19, 2017
 */
@Service
public class MeetingReviewServiceImpl extends TransactionAware implements MeetingReviewService {

	@Autowired
	private MeetingDao mDao;

	@Autowired
	private InvitationDao invitationDao;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private ResolutionDao resolutionDao;

	@Autowired
	private ExecutionDao executionDao;

	@Autowired
	private EmployeeDao empDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public List<Meeting> pastMeetings(Date currentDate) {
		return mDao.pastMeetings(currentDate);
	}

	@Override
	public List<Meeting> upComingsMeetings(Date currentDate) {
		return mDao.upComingMeetings(currentDate);
	}

	@Override
	public List<Meeting> allMeetings() {
		return mDao.findAll();
	}

	@Override
	public List<Invitation> attendees(String meetingId) {
		return invitationDao.invitationByMeeting(meetingId);
	}

	@Override
	public List<Topic> topicByMeeting(String meetingId) {
		return topicDao.topicsByMeeting(meetingId);
	}

	@Override
	public List<Resolution> resolutionByTopic(String topicId) {
		return resolutionDao.findByTopic(topicId);
	}

	@Override
	public List<Execution> executionPerResolution(String resolutionId) {
		return executionDao.executionPerResolution(resolutionId);
	}

	@Override
	public List<Employee> employeesWithExecution() {
		return empDao.findAll();
	}

	@Override
	public List<Meeting> myMeetings(String employeeEmail) {
		try {
			Employee employee = empDao.findByEmail(employeeEmail);

			List<Invitation> allInvitations = invitationDao.assignees(employee.getId());

			List<Meeting> allMyMeetings = new ArrayList<Meeting>();

			for (Invitation invitation : allInvitations) {
				allMyMeetings.add(mDao.findById(invitation.getMeeting().getId()));
			}
			return allMyMeetings;
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
	public Employee findById(String email) {
		try {
			return empDao.findByEmail(email);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Execution> allExecutions() {
		return executionDao.findAll();
	}

	

}
