/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 9, 2017
 */
@Repository(MeetingDao.NAME)
public class MeetingDao extends GenericDaoImpl<Meeting> {
	public static final String NAME = "MeetingDao";

	public static class QUERY {
		public static final String pastMeetings = "FROM Meeting m WHERE m.startTime <:today";
		public static final String todayMeeting = "FROM Meeting m WHERE m.startTime =:today";
		public static final String upComingsMeetings = "FROM Meeting m WHERE m.startTime >:today";
		public static final String findById = "FROM Meeting m WHERE m.id=:id";
	}
	

	public static class QUERY_NAME {
		public static final String pastMeetings = "MeetingDao.pastMeetings";
		public static final String todayMeeting = "MeetingDao.todayMeeting";
		public static final String findById = "MeetingDao.findById";
		public static final String upComingsMeetings = "MeetingDao.upComingsMeetings";
	}

	public Meeting findById(final String id) {
		QueryParameters props = new QueryParameters().add("id", id);
		return this.executeNamedQuerySingle(QUERY_NAME.findById, props);
	}

	public List<Meeting> pastMeetings(final Date today) {
		QueryParameters props = new QueryParameters().add("today", today);
		return this.executeNamedQueryMultiple(QUERY_NAME.pastMeetings, props);
	}

	public List<Meeting> upComingMeetings(final Date today) {
		QueryParameters props = new QueryParameters().add("today", today);
		return this.executeNamedQueryMultiple(QUERY_NAME.upComingsMeetings, props);
	}

	public List<Meeting> todayMeetings(final Date today) {
		QueryParameters props = new QueryParameters().add("today", today);
		return this.executeNamedQueryMultiple(QUERY_NAME.todayMeeting, props);
	}
}
