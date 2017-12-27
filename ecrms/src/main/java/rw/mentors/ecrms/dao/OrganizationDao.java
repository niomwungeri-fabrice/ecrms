/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 23, 2017
 */
@Repository(OrganizationDao.NAME)
public class OrganizationDao extends GenericDaoImpl<Organization> {
	public static final String NAME = "OrganizationDao";

	public static class QUERY {
		public static final String organizerPerMeeting = "From Organization where MEETING_UUID = :MEETING_UUID";
	}

	public static class QUERY_NAME {
		public static final String organizerPerMeeting = "Organization.organizerPerMeeting";
	}

	public Organization organizerPerMeeting(final String meetingId) {
		QueryParameters props = new QueryParameters().add("MEETING_UUID", meetingId);
		return this.executeNamedQuerySingle(QUERY_NAME.organizerPerMeeting, props);
	}

}
