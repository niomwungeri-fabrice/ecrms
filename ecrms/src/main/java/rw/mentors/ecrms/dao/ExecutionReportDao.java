/**
 * 
 */
package rw.mentors.ecrms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import rw.mentors.ecrms.domain.ExecutationReport;
import rw.mentors.ecrms.genericDao.GenericDaoImpl;
import rw.mentors.ecrms.util.QueryParameters;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 9, 2017
 */
@Repository(ExecutionReportDao.NAME)
public class ExecutionReportDao extends GenericDaoImpl<ExecutationReport> {
	public static final String NAME = "ExecutionReportDao";

	public static class QUERY {
		public static final String reportsByExecution = "From ExecutationReport where EXECUTION_UUI = :EXECUTION_UUI";

	}

	public static class QUERY_NAME {
		public static final String reportsByExecution = "ExecutationReport.reportsByExecution";

	}

	public List<ExecutationReport> reportsByExecution(String exeId) {
		QueryParameters props = new QueryParameters().add("EXECUTION_UUI", exeId);
		return this.executeNamedQueryMultiple(QUERY_NAME.reportsByExecution, props);
	}
}
