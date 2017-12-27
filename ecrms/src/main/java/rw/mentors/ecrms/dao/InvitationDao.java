/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 19, 2017
 */
@Repository
public class InvitationDao extends GenericDaoImpl<Invitation> {
	public static final String NAME = "InvitationDao";

	public static class QUERY {
		public static final String invitationByMeeting = "From Invitation where MEETING_UUID = :MEETING_UUID";
		public static final String assignees = "From Invitation where EMPLOYEE_UUID = :EMPLOYEE_UUID";
		
		
	}

	public static class QUERY_NAME {
		public static final String invitationByMeeting = "Invitation.invitationByMeeting";
		public static final String assignees = "Invitation.assignees";
	}

	public List<Invitation> invitationByMeeting(final String id) {
		QueryParameters props = new QueryParameters().add("MEETING_UUID", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.invitationByMeeting, props);
	}

	public List<Invitation> assignees(final String empId) {
		QueryParameters props = new QueryParameters().add("EMPLOYEE_UUID", empId);
		return this.executeNamedQueryMultiple(QUERY_NAME.assignees, props);
	}
}
