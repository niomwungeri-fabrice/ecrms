/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 16, 2017
 */
@Repository(InstitutionDao.NAME)
public class InstitutionDao extends GenericDaoImpl<Institution>{
	public static final String NAME = "InstitutionDao";

	public static class QUERY {
		public static final String findByEmail = "select e from Institution e where e.email = :email";
	}

	public static class QUERY_NAME {
		public static final String findByEmail = "Institution.findByEmail";
	}

	
	public Institution findByEmail(final String email) {
		QueryParameters props = new QueryParameters().add("email", email);
		return this.executeNamedQuerySingle(QUERY_NAME.findByEmail, props);
	}
}
