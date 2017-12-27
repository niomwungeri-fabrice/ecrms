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
import rw.mentors.ecrms.dao.ExecutionReportDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.dao.ResolutionDao;
import rw.mentors.ecrms.dao.ResourceDocumentDao;
import rw.mentors.ecrms.dao.TaskDao;
import rw.mentors.ecrms.dao.TaskReportDao;
import rw.mentors.ecrms.dao.TopicDao;
import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.ExecutationReport;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.TaskReport;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 5, 2017
 */
@Service
public class TaskReportingServiceImpl extends TransactionAware implements TaskReportingService {

	@Autowired
	private ResourceDocumentDao rdDao;
	@Autowired
	private TaskReportDao reportDao;

	@Autowired
	private ExecutionReportDao executionReportDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TopicDao topicDao;

	@Autowired
	private ResolutionDao resolutionDao;

	@Autowired
	private ExecutionDao executionDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private MeetingDao meetingDao;

	@Override
	public String taskReporting(Task selectedTask, TaskReport report, ResourceDocument resourceDocument) {
		try {
			selectedTask.setStatus(EExecutionStatus.SUBMITTED.toString());
			taskDao.update(selectedTask);
			report.setTask(selectedTask);
			System.out.println("Test Docs:" + resourceDocument.getFileName());
			if (resourceDocument.getFileName() == null) {
				reportDao.save(report);
				return "Task Submitted Successfully";
			}
			TaskReport tr = reportDao.save(report);
			resourceDocument.setTaskReport(tr);
			rdDao.update(resourceDocument);
			return "Task Submitted Successfully";

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String taskAproval(TaskReport report, Task task, ResourceDocument resourceDocument) {
		try {
			task.setStatus(EExecutionStatus.APPROVED.toString());
			taskDao.update(task);
			report.setTask(task);
			if (resourceDocument.getFileName() == null) {
				reportDao.save(report);
				return "Task Approved Successfully";
			}
			TaskReport tr = reportDao.save(report);
			resourceDocument.setTaskReport(tr);
			rdDao.update(resourceDocument);
			return "Task Approved Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String taskRejection(TaskReport report, Task task, ResourceDocument resourceDocument) {
		try {
			task.setStatus(EExecutionStatus.REJECTED.toString());
			taskDao.update(task);
			report.setTask(task);
			if (resourceDocument.getFileName() == null) {
				reportDao.save(report);
				return "Task Rejected Successfully";
			}
			TaskReport tr = reportDao.save(report);
			resourceDocument.setTaskReport(tr);
			rdDao.update(resourceDocument);
			return "Task Rejected Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String updateTask(Task task) {
		try {
			taskDao.update(task);
			return "Task Opened, Now In progress";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Execution> executionByResolution(String meetingId) {
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
	public String updateExecution(Execution execution) {
		try {
			executionDao.update(execution);
			return "Execution Opened, Now In progress";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String executionReporting(Execution execution, ExecutationReport executationReport,
			ResourceDocument resourceDocument, String email) {
		try {
			execution.setStatus(EExecutionStatus.SUBMITTED.toString());
			executionDao.update(execution);
			Employee emp = employeeDao.findByEmail(email);
			executationReport.setEmployee(emp);
			executationReport.setExecution(execution);
			
			if (resourceDocument.getFileName() == null) {
				executionReportDao.save(executationReport);
				return "Execution Submitted Successfully";
			}
			ExecutationReport er = executionReportDao.save(executationReport);
			resourceDocument.setExReport(er);
			rdDao.update(resourceDocument);
			return "Execution Submitted Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<TaskReport> reportsByTask(String taskId) {
		try {
			return reportDao.reportsByTask(taskId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<ExecutationReport> reportsByExecution(String exeId) {
		try {
			return executionReportDao.reportsByExecution(exeId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String executionAproval(ExecutationReport report, Execution execution, ResourceDocument resourceDocument, String email) {
		try {
			execution.setStatus(EExecutionStatus.APPROVED.toString());
			executionDao.update(execution);
			Employee emp = employeeDao.findByEmail(email);
			report.setEmployee(emp);
			report.setExecution(execution);
			if (resourceDocument.getFileName() == null) {
				executionReportDao.save(report);
				return "Execution Approved Successfully";
			}
			ExecutationReport er = executionReportDao.save(report);
			resourceDocument.setExReport(er);
			rdDao.update(resourceDocument);
			return "Execution Approved Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String executionRejection(ExecutationReport report, Execution execution, ResourceDocument resourceDocument,String email) {
		try {
			execution.setStatus(EExecutionStatus.REJECTED.toString());
			executionDao.update(execution);
			Employee emp = employeeDao.findByEmail(email);
			report.setEmployee(emp);
			report.setExecution(execution);
			if (resourceDocument.getFileName() == null) {
				executionReportDao.save(report);
				return "Execution Rejected Successfully";
			}
			ExecutationReport er = executionReportDao.save(report);
			resourceDocument.setExReport(er);
			rdDao.update(resourceDocument);
			return "Execution Rejected Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<ResourceDocument> resourcePerTaskReport(String taskReportId) {
		try {
			return rdDao.findTaskDocs(taskReportId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<ResourceDocument> resourcePerExecutionReport(String exeReportId) {
		try {
			return rdDao.findExeDocs(exeReportId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<ResourceDocument> findTaskAttachments(String taskId) {
		try {
			return rdDao.findTaskAttachments(taskId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<ResourceDocument> findExecutionAttachments(String executionId) {
		try {
			return rdDao.findExecutionAttachments(executionId);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<Employee> findAllEmployee() {
		return employeeDao.findAll();
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
	public List<Meeting> findAllMeetings() {
		return meetingDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rw.mentors.ecrms.service.TaskReportingService#findByEmail(java.lang.
	 * String)
	 */
	@Override
	public Employee findByEmail(String email) {
		try {
			return employeeDao.findByEmail(email);
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
		
	}

	@Override
	public String executionUpdated(ExecutationReport report, Execution execution, ResourceDocument resourceDocument,
			String commentBy) {
		try {
			executionDao.update(execution);
			Employee emp = employeeDao.findByEmail(commentBy);
			report.setEmployee(emp);
			report.setExecution(execution);
			if (resourceDocument.getFileName() == null) {
				executionReportDao.save(report);
				return "Execution Updated Successfully";
			}
			ExecutationReport er = executionReportDao.save(report);
			resourceDocument.setExReport(er);
			rdDao.update(resourceDocument);
			return "Execution Updated Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	
}
