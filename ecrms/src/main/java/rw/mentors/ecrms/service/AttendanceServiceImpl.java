/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.InvitationDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 26, 2017
 */
@Service
public class AttendanceServiceImpl extends TransactionAware implements AttendanceService {

	@Autowired
	private InvitationDao invitationDao;

	@Autowired
	private MeetingDao meetingDao;

	@Override
	public List<Invitation> findInvited(String id) {
		return invitationDao.invitationByMeeting(id);
	}

	@Override
	public String checkAllBoxes(Invitation invitation) {
		try {
			invitationDao.update(invitation);
			return "All Participants attended";
		} catch (Exception e) {
			throw new DataManipulationException("Error!!, Contact System admin>>>" + e.getMessage());
		}

	}

	@Override
	public Invitation findById(String id) {
		try {
			Invitation invitation = invitationDao.findOne(id);
			return invitation;
		} catch (Exception e) {
			throw new DataManipulationException("Object Not found");
		}

	}

	@Override
	public List<Meeting> findAllMeetings() {
		return meetingDao.findAll();
	}

	@Override
	public List<Invitation> invitations() {

		return invitationDao.findAll();
	}

	@Override
	public String updateInvitation(Invitation invitation) {
		try {
			invitationDao.update(invitation);
			if (invitation.isConfirmed()) {
				return "Your attendanced is confirmed";
			} else {
				return "your attendance is NOT confirmed";
			}
		} catch (Exception e) {
			throw new DataManipulationException("Object Not found");
		}
	}

}
