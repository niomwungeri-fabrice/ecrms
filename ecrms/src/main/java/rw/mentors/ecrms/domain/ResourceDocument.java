/**
 * 
 */
package rw.mentors.ecrms.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import rw.mentors.ecrms.dao.ResourceDocumentDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "RESOURCE_DOCUMENT")
@NamedQueries({
		@NamedQuery(name = ResourceDocumentDao.QUERY_NAME.findTaskReports, query = ResourceDocumentDao.QUERY.findTaskReports),
		@NamedQuery(name = ResourceDocumentDao.QUERY_NAME.findExeReports, query = ResourceDocumentDao.QUERY.findExeReports),
		@NamedQuery(name = ResourceDocumentDao.QUERY_NAME.findTaskAttachments, query = ResourceDocumentDao.QUERY.findTaskAttachments),
		@NamedQuery(name = ResourceDocumentDao.QUERY_NAME.findExecutionAttachments, query = ResourceDocumentDao.QUERY.findExecutionAttachments) })
public class ResourceDocument extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "DOCUMENT_NAME", nullable = false)
	private String documentName;
	
	@Column(name = "FILE_NAME", nullable = false)
	private String fileName;
	
	@Column(name = "FILE_TYPE", nullable = false)
	private String fileType;
	
	@Column(name = "FILE_CONTENT", nullable = false)
	@Lob
	private byte[] fileContent;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PRESENTATION_UUID", nullable = true)
	private Presentation presentation;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "EXECUTION_REPORT_UUID", nullable = true)
	private ExecutationReport exReport;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "EXECUTION_UUID", nullable = true)
	private Execution execution;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TASK_REPORT_UUID", nullable = true)
	private TaskReport taskReport;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TASK_UUID", nullable = true)
	private Task task;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public ExecutationReport getExReport() {
		return exReport;
	}

	public void setExReport(ExecutationReport exReport) {
		this.exReport = exReport;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public TaskReport getTaskReport() {
		return taskReport;
	}

	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}

}
