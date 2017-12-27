
package rw.mentors.ecrms.domain;

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

import rw.mentors.ecrms.dao.ResolutionDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "RESOLUTION")
@NamedQueries({ @NamedQuery(name = ResolutionDao.QUERY_NAME.findByTopic, query = ResolutionDao.QUERY.findByTopic) })
public class Resolution extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name="TYPE")
	private String type;
	@ManyToOne(cascade = CascadeType.ALL)
	
	@JoinColumn(name = "TOPIC_UUID")
	private Topic topic;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "resolution")
	private List<Execution> execution;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Execution> getExecution() {
		return execution;
	}

	public void setExecution(List<Execution> execution) {
		this.execution = execution;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
