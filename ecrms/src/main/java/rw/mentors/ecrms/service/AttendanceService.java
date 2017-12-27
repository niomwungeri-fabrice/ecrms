/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 26, 2017
 */
public interface AttendanceService {
	List<Invitation> findInvited(String meetingId);

	String checkAllBoxes(Invitation invitation);

	Invitation findById(String id);

	List<Meeting> findAllMeetings();

	List<Invitation> invitations();

	String updateInvitation(Invitation invitation);

}
