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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import rw.mentors.ecrms.dao.ExecutionDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "EXECUTION", uniqueConstraints = @UniqueConstraint(columnNames = { "ASSIGNED_TO", "RESOLUTION_UUID",
		"ASSIGNED_BY" }))
@NamedQueries({
		@NamedQuery(name = ExecutionDao.QUERY_NAME.executionPerResolution, query = ExecutionDao.QUERY.executionPerResolution),
		@NamedQuery(name = ExecutionDao.QUERY_NAME.countExecution, query = ExecutionDao.QUERY.countExecution),
		@NamedQuery(name = ExecutionDao.QUERY_NAME.findById, query = ExecutionDao.QUERY.findById) })
public class Execution extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "START_DATE")
	private Date startDate;
	@Column(name = "END_DATE")
	private Date endDate;
	@Column(name = "GIVEN_DATE", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date givenDate;
	@Column(name = "STATUS", nullable = false)
	private String status;
	@Column(name = "WEIGHT", nullable = true)
	private Integer weight;
	@Column(name = "RATE", nullable = true)
	private Integer rate;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ASSIGNED_TO", nullable = true)
	private Employee assignedTo;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ASSIGNED_BY", nullable = true)
	private Employee assignedBy;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "execution")
	private List<ResourceDocument> accompaniedFiles;
	
	@OneToMany(mappedBy = "execution")
	private List<ExecutationReport> exeReport;

	@OneToMany(mappedBy = "execution")
	private List<ExecutionEvaluation> evaluations;

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "RESOLUTION_UUID")
	private Resolution resolution;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Employee getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Employee assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Employee getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(Employee assignedBy) {
		this.assignedBy = assignedBy;
	}

	public List<ExecutationReport> getExeReport() {
		return exeReport;
	}

	public void setExeReport(List<ExecutationReport> exeReport) {
		this.exeReport = exeReport;
	}

	public Date getGivenDate() {
		return givenDate;
	}

	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public List<ResourceDocument> getAccompaniedFiles() {
		return accompaniedFiles;
	}

	public void setAccompaniedFiles(List<ResourceDocument> accompaniedFiles) {
		this.accompaniedFiles = accompaniedFiles;
	}

	public List<ExecutionEvaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<ExecutionEvaluation> evaluations) {
		this.evaluations = evaluations;
	}

}
