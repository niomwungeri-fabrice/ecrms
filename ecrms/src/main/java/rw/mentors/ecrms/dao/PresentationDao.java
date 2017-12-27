/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Presentation;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 23, 2017
 */
@Repository(PresentationDao.NAME)
public class PresentationDao extends GenericDaoImpl<Presentation> {
	public static final String NAME = "PresentationDao";

	public static class QUERY {
		public static final String presentationByPresenter = "From Presentation where PRESENTER_UUID = :PRESENTER_UUID";
																	
	}

	public static class QUERY_NAME {
		public static final String presentationByPresenter = "Presentation.presentationByPresenter";

	}

	public Presentation presentationByPresenter(final String presenterId) {
		QueryParameters props = new QueryParameters().add("PRESENTER_UUID", presenterId);
		return this.executeNamedQuerySingle(QUERY_NAME.presentationByPresenter, props);
	}
}
