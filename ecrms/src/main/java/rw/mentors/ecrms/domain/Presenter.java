/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import rw.mentors.ecrms.dao.PresenterDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 22, 2017
 */
@Entity
@Table(name = "PRESENTER")
@NamedQueries({ @NamedQuery(name = PresenterDao.QUERY_NAME.presenterById, query = PresenterDao.QUERY.presenterById) })
public class Presenter extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "EMPLOYEE_UUID", nullable = true)
	private Employee employee;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "INSTITUTION_UUID", nullable = true)
	private Institution institution;

	public List<Presentation> getPresentations() {
		return presentations;
	}

	public void setPresentations(List<Presentation> presentations) {
		this.presentations = presentations;
	}

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "presenter")
	private List<Topic> topics;

	@OneToMany(mappedBy = "presenter")
	private List<Presentation> presentations;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

}
