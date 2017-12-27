/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Presenter;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Sep 21, 2017
 */
@Repository(PresenterDao.NAME)
public class PresenterDao extends GenericDaoImpl<Presenter> {
	public static final String NAME = "PresenterDao";

	public static class QUERY {
		public static final String presenterById = "select e from Presenter e where e.id = :id";

	}

	public static class QUERY_NAME {
		public static final String presenterById = "Presenter.presenterById";

	}

	public Presenter presenterById(final String id) {
		QueryParameters props = new QueryParameters().add("id", id);
		return this.executeNamedQuerySingle(QUERY_NAME.presenterById, props);
	}
}
