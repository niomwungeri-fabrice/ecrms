/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.MeetingType;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 9, 2017
 */
@Repository
public class MeetingTypeDao extends GenericDaoImpl<MeetingType> {
	public static final String NAME = "MeetingTypeDao";

	public static class QUERY {
		public static final String findById = "select e from MeetingType e where e.id = :id";
	}

	public static class QUERY_NAME {
		public static final String findById = "MeetingType.MeetingTypeDao";
	}

	public MeetingType findById(final String email) {
		QueryParameters props = new QueryParameters().add("id", email);
		return this.executeNamedQuerySingle(QUERY_NAME.findById, props);
	}
}
