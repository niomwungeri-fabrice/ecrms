/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.UserRoles;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 19, 2017
 */
@Repository(UserRoleDao.NAME)
public class UserRoleDao extends GenericDaoImpl<UserRoles> {
	public static final String NAME = "UserRoleDao";

	public static class QUERY {
		public static final String userRoles = "From UserRoles where USER_UUID = :USER_UUID";

	}

	public static class QUERY_NAME {
		public static final String userRoles = "UserRoles.userRoles";

	}

	public List<UserRoles> userRoles(final String id) {
		QueryParameters props = new QueryParameters().add("USER_UUID", id);
		return this.executeNamedQueryMultiple(QUERY_NAME.userRoles, props);
	}

}
