/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 25, 2017
 */
@Repository(ResolutionDao.NAME)
public class ResolutionDao extends GenericDaoImpl<Resolution> {
	
	public static final String NAME = "ResolutionDao";

	public static class QUERY {
		public static final String findByTopic = "From Resolution where TOPIC_UUID = :TOPIC_UUID";
	}

	public static class QUERY_NAME {
		public static final String findByTopic = "Resolution.findByTopic";
	}

	public List<Resolution> findByTopic(final String id) {
		QueryParameters props = new QueryParameters().add("TOPIC_UUID", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.findByTopic, props);
	}
	
}
