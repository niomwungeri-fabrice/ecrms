/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.EmployeeDao;
import rw.mentors.ecrms.dao.ExecutionDao;
import rw.mentors.ecrms.dao.InstitutionDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.dao.ResolutionDao;
import rw.mentors.ecrms.dao.ResourceDocumentDao;
import rw.mentors.ecrms.dao.TopicDao;
import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 24, 2017
 */
@Service
public class MeetingMinuteServiceImpl extends TransactionAware implements MeetingMinuteService {

	@Autowired
	private ResourceDocumentDao rdDao;
	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private ResolutionDao resolutionDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private ExecutionDao executionDao;

	@Autowired
	private InstitutionDao instaDao;

	@Override
	public List<Meeting> findAllMeetings() {
		return meetingDao.findAll();
	}

	@Override
	public List<Topic> topicsByMeetingList(String meeting) {

		return topicDao.topicsByMeeting(meeting);
	}

	@Override
	public List<Resolution> resolutionPerTopic(String topic) {
		return resolutionDao.findByTopic(topic);
	}

	@Override
	public Employee findByEmail(String email) {
		return employeeDao.findByEmail(email);
	}

	@Override
	public List<Employee> findAllEmployee() {

		return employeeDao.findAll();
	}

	@Override
	public List<Execution> findAllExecution() {

		return executionDao.findAll();
	}

	@Override
	public List<Institution> findAllInsta() {
		return instaDao.findAll();
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
	public Meeting meetingById(String uuid) {
		try {
			return meetingDao.findById(uuid);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String deleteResolution(Resolution resolution) {
		try {
			for (Execution execution : executionDao.executionPerResolution(resolution.getId())) {
				executionDao.delete(execution);
			}
			resolutionDao.delete(resolution);
			return "Resolution Removed Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	public String createResolution(Resolution resolution, Execution execution, String assignedBy, String assignedTo,
			ResourceDocument document) {
		try {

			if (assignedBy.equals("") && assignedTo.equals("") && document.getFileName() == null) {
				resolutionDao.save(resolution);
				return "Execution added Successfully";
			} else if (document.getFileName() != null) {
				Resolution reso = resolutionDao.save(resolution);
				Employee to = employeeDao.findByEmail(assignedTo);
				Employee by = employeeDao.findByEmail(assignedBy);
				execution.setAssignedBy(by);
				execution.setAssignedTo(to);
				execution.setResolution(reso);
				execution.setStatus(EExecutionStatus.PENDING.toString());
				Execution ex = executionDao.save(execution);
				document.setExecution(ex);
				rdDao.update(document);
				return "Execution added Successfully";
			} else {
				Resolution reso = resolutionDao.save(resolution);
				Employee to = employeeDao.findByEmail(assignedTo);
				Employee by = employeeDao.findByEmail(assignedBy);
				execution.setAssignedBy(by);
				execution.setAssignedTo(to);
				execution.setResolution(reso);
				execution.setStatus(EExecutionStatus.PENDING.toString());
				executionDao.save(execution);
				return "Execution added Successfully";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String editResolution(Resolution resolution, Execution execution, String assignedBy, String assignedTo,
			ResourceDocument document) {
		if (assignedBy.equals("") && assignedTo.equals("") && document.getFileName() == null) {
			resolutionDao.update(resolution);
			executionDao.update(execution);
			return "Execution updated Successfully";
		} else if (!assignedBy.equals("")) {
			Employee by = employeeDao.findByEmail(assignedBy);
			execution.setAssignedBy(by);
			Resolution resolution2 = resolutionDao.update(resolution);
			execution.setResolution(resolution2);
			executionDao.update(execution);
			return "Execution updated Successfully";
		} else if (!assignedTo.equals("")) {
			Employee to = employeeDao.findByEmail(assignedTo);
			execution.setAssignedTo(to);
			Resolution resolution2 = resolutionDao.update(resolution);
			execution.setResolution(resolution2);
			executionDao.update(execution);
			return "Execution updated Successfully";
		} else if (document.getFileName() != null) {
			Resolution reso = resolutionDao.update(resolution);
			Employee to = employeeDao.findByEmail(assignedTo);
			Employee by = employeeDao.findByEmail(assignedBy);
			execution.setAssignedBy(by);
			execution.setAssignedTo(to);
			execution.setResolution(reso);
			execution.setStatus(EExecutionStatus.PENDING.toString());
			Execution ex = executionDao.update(execution);
			document.setExecution(ex);
			rdDao.update(document);
			return "Execution updated Successfully";
		} else {
			Resolution reso = resolutionDao.update(resolution);
			Employee to = employeeDao.findByEmail(assignedTo);
			Employee by = employeeDao.findByEmail(assignedBy);
			execution.setAssignedBy(by);
			execution.setAssignedTo(to);
			execution.setResolution(reso);
			execution.setStatus(EExecutionStatus.PENDING.toString());
			executionDao.update(execution);
			return "Execution updated Successfully";
		}

	}

}
