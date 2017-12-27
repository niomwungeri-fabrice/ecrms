/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import rw.mentors.ecrms.dao.MeetingTypeDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 9, 2017
 */
@Entity
@Table(name = "MEETING_TYPE")
@NamedQueries({ @NamedQuery(name = MeetingTypeDao.QUERY_NAME.findById, query = MeetingTypeDao.QUERY.findById) })
public class MeetingType extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "meetingType")
	private List<Meeting> meeting;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Meeting> getMeeting() {
		return meeting;
	}

	public void setMeeting(List<Meeting> meeting) {
		this.meeting = meeting;
	}

}
