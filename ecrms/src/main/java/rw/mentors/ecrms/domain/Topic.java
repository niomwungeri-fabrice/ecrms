
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

import rw.mentors.ecrms.dao.TopicDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "TOPIC")
@NamedQueries({ @NamedQuery(name = TopicDao.QUERY_NAME.topicsByMeeting, query = TopicDao.QUERY.topicsByMeeting),
		@NamedQuery(name = TopicDao.QUERY_NAME.topicById, query = TopicDao.QUERY.topicById) })
public class Topic extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME")
	private String name;
	@Column(name = "OBJECTIVE")
	private String objective;
	@Column(name = "START_TIME",unique = true)
	private Date startTime;
	@Column(name = "END_TIME")
	private Date endTime;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "MEETING_UUID", nullable = false)
	private Meeting meeting;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PRESENTER_UUID")
	private Presenter presenter;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "topic")
	private List<Resolution> resolutions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	/**
	 * @return the meeting
	 */
	public Meeting getMeeting() {
		return meeting;
	}

	/**
	 * @param meeting
	 *            the meeting to set
	 */
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Presenter getPresenter() {
		return presenter;
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
