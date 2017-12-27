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

import rw.mentors.ecrms.dao.ExecutionReportDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "EXECUTATION_REPORT")
@NamedQueries({
		@NamedQuery(name = ExecutionReportDao.QUERY_NAME.reportsByExecution, query = ExecutionReportDao.QUERY.reportsByExecution) })
public class ExecutationReport extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "DATE", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date commentDate;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "EXECUTION_UUI", nullable = false)
	private Execution execution;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "exReport")
	private List<ResourceDocument> rDocument;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "EMPLOYEE_UUI")
	private Employee employee;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public List<ResourceDocument> getrDocument() {
		return rDocument;
	}

	public void setrDocument(List<ResourceDocument> rDocument) {
		this.rDocument = rDocument;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	

}
