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

import rw.mentors.ecrms.dao.PresentationDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "PRESENTATION")
@NamedQueries({
		@NamedQuery(name = PresentationDao.QUERY_NAME.presentationByPresenter, query = PresentationDao.QUERY.presentationByPresenter) })
public class Presentation extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PRESENTER_UUID")
	private Presenter presenter;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "presentation")
	private List<ResourceDocument> rDocuments;

	public Presenter getPresenter() {
		return presenter;
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public List<ResourceDocument> getrDocuments() {
		return rDocuments;
	}

	public void setrDocuments(List<ResourceDocument> rDocuments) {
		this.rDocuments = rDocuments;
	}

}
