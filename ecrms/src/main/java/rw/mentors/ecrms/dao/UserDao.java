/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.User;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 15, 2017
 */
@Repository(UserDao.NAME)
public class UserDao extends GenericDaoImpl<User> {
	public static final String NAME = "UserDao";

	public static class QUERY {
		public static final String findById = "select e from User e where e.id = :id";
		public static final String findByUsername = "select e from User e where e.username = :username";

	}

	public static class QUERY_NAME {
		public static final String findById = "User.findById";
		public static final String findByUsername = "User.findByUsername";

	}

	public User findById(final String id) {
		QueryParameters props = new QueryParameters().add("id", id);
		return this.executeNamedQuerySingle(QUERY_NAME.findById, props);
	}

	public User findByUsername(final String username) {
		QueryParameters props = new QueryParameters().add("username", username);
		return this.executeNamedQuerySingle(QUERY_NAME.findByUsername, props);
	}
}
