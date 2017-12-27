/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import rw.mentors.ecrms.dao.TaskReportDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 5, 2017
 */
@Entity
@Table(name = "TASK_REPORT")
@NamedQueries({ @NamedQuery(name = TaskReportDao.QUERY_NAME.reportsByTask, query = TaskReportDao.QUERY.reportsByTask) })
public class TaskReport extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "COMMENT")
	private String comment;
	@Column(name = "DATE", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date commentDate;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TASK_UUID")
	private Task task;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "taskReport")
	private List<ResourceDocument> taskReports;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public List<ResourceDocument> getTaskReports() {
		return taskReports;
	}

	public void setTaskReports(List<ResourceDocument> taskReports) {
		this.taskReports = taskReports;
	}

}
