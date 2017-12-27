/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.ExpenditureItem;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 13, 2017
 */
@Repository(ExpenditureItemDao.NAME)
public class ExpenditureItemDao extends GenericDaoImpl<ExpenditureItem> {

	public static final String NAME = "ExpenditureItemDao";

	public static class QUERY {
		public static final String costByMeeting = "From ExpenditureItem where meeting_UUID = :meeting_UUID";

	}

	public static class QUERY_NAME {
		public static final String costByMeeting = "ExpenditureItem.costByMeeting";

	}
	public List<ExpenditureItem> costByMeeting(String meetingId) {
		QueryParameters props = new QueryParameters().add("meeting_UUID", meetingId);
		return this.executeNamedQueryMultiple(QUERY_NAME.costByMeeting, props);
	}
}
