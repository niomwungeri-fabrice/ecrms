package rw.mentors.ecrms.dao;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 6, 2017
 */
@Repository(EmployeeDao.NAME)
public class EmployeeDao extends GenericDaoImpl<Employee> {

	public static final String NAME = "EmployeeDao";

	public static class QUERY {
		public static final String findByEmail = "select e from Employee e where e.email = :email";

	}

	public static class QUERY_NAME {
		public static final String findByEmail = "Employee.findByEmail";

	}

	public Employee findByEmail(final String email) {
		QueryParameters props = new QueryParameters().add("email", email);
		return this.executeNamedQuerySingle(QUERY_NAME.findByEmail, props);
	}
}
