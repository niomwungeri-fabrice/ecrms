/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 29, 2017
 */
@Repository(ExecutionDao.NAME)
public class ExecutionDao extends GenericDaoImpl<Execution> {

	public static final String NAME = "ExecutionDao";

	public static class QUERY {
		public static final String executionPerResolution = "From Execution where RESOLUTION_UUID = :RESOLUTION_UUID";
		public static final String countExecution = "From Execution where STATUS = :STATUS";
		public static final String findById = "From Execution where UUID = :UUID";
	}

	public static class QUERY_NAME {
		public static final String executionPerResolution = "Execution.executionPerResolution";
		public static final String countExecution = "Execution.countExecution";
		public static final String findById = "Execution.findById";
	}

	public List<Execution> executionPerResolution(String topicId) {
		QueryParameters props = new QueryParameters().add("RESOLUTION_UUID", topicId);
		return this.executeNamedQueryMultiple(QUERY_NAME.executionPerResolution, props);
	}

	public List<Execution> countExection(final String id) {
		QueryParameters props = new QueryParameters().add("STATUS", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.countExecution, props);
	}

	public Execution findById(final String uuid) {
		QueryParameters props = new QueryParameters().add("UUID", uuid);
		return this.executeNamedQuerySingle(QUERY_NAME.findById, props);
	}

}
