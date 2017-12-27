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
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.dao.ResolutionDao;
import rw.mentors.ecrms.dao.TaskDao;
import rw.mentors.ecrms.dao.TopicDao;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 7, 2017
 */
@Service
public class StatisticServiceImpl extends TransactionAware implements StatisticService {

	@Autowired
	private EmployeeDao empDao;
	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private ExecutionDao executionDao;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private ResolutionDao resolutionDao;

	@Autowired
	private TaskDao taskDao;

	@Override
	public List<Meeting> allMeetings() {
		return meetingDao.findAll();
	}

	@Override
	public List<Execution> executionPerResolution(String meetingId) {
		try {
			List<Resolution> r = new ArrayList<Resolution>();
			List<Execution> reso = new ArrayList<Execution>();
			for (Topic topic : topicDao.topicsByMeeting(meetingId)) {
				for (Resolution resolution : resolutionDao.findByTopic(topic.getId())) {
					r.add(resolution);
				}
			}
			for (Resolution res : r) {
				for (Execution e : executionDao.executionPerResolution(res.getId())) {
					reso.add(e);
				}
			}
			return reso;
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public Meeting findMeetingById(String id) {
		try {
			return meetingDao.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Task> taskByMeeting(String meetingId) {
		try {
			return taskDao.taskByMeeting(meetingId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Execution> findAllExecutions() {
		return executionDao.findAll();
	}

	@Override
	public List<Employee> findAllEmployee() {
		return empDao.findAll();
	}

}
