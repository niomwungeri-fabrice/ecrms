/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 10, 2017
 */
@Repository(ResourceDocumentDao.NAME)
public class ResourceDocumentDao extends GenericDaoImpl<ResourceDocument> {

	public static final String NAME = "ResourceDocumentDao";

	public static class QUERY {
		public static final String findTaskReports = "From ResourceDocument where TASK_REPORT_UUID = :TASK_REPORT_UUID";
		public static final String findExeReports = "From ResourceDocument where EXECUTION_REPORT_UUID = :EXECUTION_REPORT_UUID";
		public static final String findTaskAttachments = "From ResourceDocument where TASK_UUID = :TASK_UUID";
		public static final String findExecutionAttachments = "From ResourceDocument where EXECUTION_UUID = :EXECUTION_UUID";

	}

	public static class QUERY_NAME {
		public static final String findTaskReports = "ResourceDocument.findTaskReports";
		public static final String findExeReports = "ResourceDocument.findExeReports";
		public static final String findTaskAttachments = "ResourceDocument.findTaskAttachments";
		public static final String findExecutionAttachments = "ResourceDocument.findExecutionAttachments";
	}

	public List<ResourceDocument> findTaskDocs(String taskReportId) {
		QueryParameters props = new QueryParameters().add("TASK_REPORT_UUID", taskReportId);
		return this.executeNamedQueryMultiple(QUERY_NAME.findTaskReports, props);
	}

	public List<ResourceDocument> findExeDocs(String exeReportId) {
		QueryParameters props = new QueryParameters().add("EXECUTION_REPORT_UUID", exeReportId);
		return this.executeNamedQueryMultiple(QUERY_NAME.findExeReports, props);
	}

	public List<ResourceDocument> findExecutionAttachments(String executionId) {
		QueryParameters props = new QueryParameters().add("EXECUTION_UUID", executionId);
		return this.executeNamedQueryMultiple(QUERY_NAME.findExecutionAttachments, props);
	}

	public List<ResourceDocument> findTaskAttachments(String taskId) {
		QueryParameters props = new QueryParameters().add("TASK_UUID", taskId);
		return this.executeNamedQueryMultiple(QUERY_NAME.findTaskAttachments, props);
	}
}
