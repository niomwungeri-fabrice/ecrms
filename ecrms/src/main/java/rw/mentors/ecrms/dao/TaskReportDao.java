/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.TaskReport;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 5, 2017
 */
@Repository(TaskReportDao.NAME)
public class TaskReportDao extends GenericDaoImpl<TaskReport> {
	
	public static final String NAME = "TaskReportDaoo";

	public static class QUERY {
		public static final String reportsByTask = "From TaskReport where TASK_UUID = :TASK_UUID";

	}

	public static class QUERY_NAME {
		public static final String reportsByTask = "TaskReport.reportsByTask";

	}

	public List<TaskReport> reportsByTask(String taskId) {
		QueryParameters props = new QueryParameters().add("TASK_UUID", taskId);
		return this.executeNamedQueryMultiple(QUERY_NAME.reportsByTask, props);
	}
}
