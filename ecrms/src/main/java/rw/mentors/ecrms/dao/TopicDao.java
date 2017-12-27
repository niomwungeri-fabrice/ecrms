/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 15, 2017
 */
@Repository(TopicDao.NAME)
public class TopicDao extends GenericDaoImpl<Topic> {
	public static final String NAME = "TopicDao";

	public static class QUERY {
		public static final String topicsByMeeting = "From Topic where MEETING_UUID = :MEETING_UUID";
		public static final String topicById = "From Topic where UUID = :UUID";
	}

	public static class QUERY_NAME {
		public static final String topicsByMeeting = "Topic.topicsByMeeting";
		public static final String topicById = "Topic.topicById";
	}

	public List<Topic> topicsByMeeting(final String id) {
		QueryParameters props = new QueryParameters().add("MEETING_UUID", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.topicsByMeeting, props);
	}

	public Topic topicById(final String uuid) {
		QueryParameters props = new QueryParameters().add("UUID", uuid);
		return this.executeNamedQuerySingle(QUERY_NAME.topicById, props);
	}
}
