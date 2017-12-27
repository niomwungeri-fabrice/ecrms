/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 29, 2017
 */
@Repository(TaskDao.NAME)
public class TaskDao extends GenericDaoImpl<Task> {
	
	public static final String NAME = "TaskDao";

	public static class QUERY {
		public static final String taskByMeeting = "From Task where MEETING_UUID = :MEETING_UUID";
		public static final String countPending = "From Task where STATUS = :STATUS";
		
	}

	public static class QUERY_NAME {
		public static final String taskByMeeting = "Task.taskByMeeting";
		public static final String countPending = "Task.countPending";
	}

	public List<Task> taskByMeeting(final String id) {
		QueryParameters props = new QueryParameters().add("MEETING_UUID", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.taskByMeeting, props);
	}
	public List<Task> countPending(final String id) {
		QueryParameters props = new QueryParameters().add("STATUS", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.countPending, props);
	}
	
}
