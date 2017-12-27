/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.ExecutationReport;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.TaskReport;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 5, 2017
 */
@Service
public interface TaskReportingService {

	// tasks
	String taskReporting(Task task, TaskReport report, ResourceDocument resourceDocument);

	String taskAproval(TaskReport report, Task task, ResourceDocument resourceDocument);

	String taskRejection(TaskReport report, Task task, ResourceDocument resourceDocument);

	String updateTask(Task task);

	List<TaskReport> reportsByTask(String taskId);

	// execution

	String executionAproval(ExecutationReport report, Execution task, ResourceDocument resourceDocument,
			String commentBy);
	
	String executionUpdated(ExecutationReport report, Execution task, ResourceDocument resourceDocument,
			String commentBy);
	

	String executionRejection(ExecutationReport report, Execution task, ResourceDocument resourceDocument,
			String commentBy);

	String executionReporting(Execution execution, ExecutationReport executationReport,
			ResourceDocument resourceDocument, String commentBy);

	List<ExecutationReport> reportsByExecution(String exeId);

	String updateExecution(Execution execution);

	List<Execution> executionByResolution(String meetingId);


	// resource

	List<ResourceDocument> resourcePerExecutionReport(String exeReportId);

	List<ResourceDocument> resourcePerTaskReport(String taskReportId);

	List<ResourceDocument> findTaskAttachments(String taskId);

	List<ResourceDocument> findExecutionAttachments(String executionId);

	ResourceDocument fileSaver(ResourceDocument resourceDocument);

	// employee

	List<Employee> findAllEmployee();

	Employee findByEmail(String email);

	// meetings
	List<Meeting> findAllMeetings();

}
