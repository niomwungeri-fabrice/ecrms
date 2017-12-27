/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
public enum EExecutionStatus {
	PENDING, IN_PROGROCESS, SUBMITTED, APPROVED, REJECTED, UPDATED;

	public static List<EExecutionStatus> getExecutionStatus() {
		List<EExecutionStatus> list = new ArrayList<EExecutionStatus>();
		EExecutionStatus[] arr = EExecutionStatus.values();
		list.addAll(Arrays.asList(arr));
		return list;
	}
}
