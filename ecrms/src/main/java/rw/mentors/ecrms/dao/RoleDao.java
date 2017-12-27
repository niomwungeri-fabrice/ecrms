/**
 * 
 */
package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Role;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 19, 2017
 */
@Repository(RoleDao.NAME)
public class RoleDao extends GenericDaoImpl<Role> {
	public static final String NAME = "RoleDao";

	public static class QUERY {
		public static final String findRoleById = "select e from Role e where e.id = :id";

	}

	public static class QUERY_NAME {
		public static final String findRoleById = "Role.findById";

	}

	public Role findRoleById(final String id) {
		QueryParameters props = new QueryParameters().add("id", id);
		return this.executeNamedQuerySingle(QUERY_NAME.findRoleById, props);
	}
}
