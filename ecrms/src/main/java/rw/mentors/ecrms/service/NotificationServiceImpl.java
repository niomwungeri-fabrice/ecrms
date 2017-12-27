/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.EmployeeDao;
import rw.mentors.ecrms.dao.ExecutionDao;
import rw.mentors.ecrms.dao.InvitationDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.dao.TaskDao;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 7, 2017
 */
@Service
public class NotificationServiceImpl extends TransactionAware implements NotificationService {

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private ExecutionDao executionDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private InvitationDao invitationDao;


	@Override
	public List<Task> countTasks(String status) {
		return taskDao.countPending(status);
	}

	@Override
	public List<Execution> countExecution(String status) {
		return executionDao.countExection(status);
	}

	@Override
	public List<Meeting> findAllExecution() {
		return meetingDao.findAll();
	}

	
	@Override
	public List<Meeting> myMeetings(String employeeEmail) {
		try {
			Employee employee = employeeDao.findByEmail(employeeEmail);
			List<Invitation> allInvitations = invitationDao.assignees(employee.getId());
			List<Meeting> allMyMeetings = new ArrayList<Meeting>();
			for (Invitation invitation : allInvitations) {
				allMyMeetings.add(meetingDao.findById(invitation.getMeeting().getId()));
			}
			return allMyMeetings;
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

}
