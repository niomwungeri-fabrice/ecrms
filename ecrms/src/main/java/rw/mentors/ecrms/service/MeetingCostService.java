
package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.ExpenditureItem;
import rw.mentors.ecrms.domain.Meeting;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 13, 2017
 */
public interface MeetingCostService {
	
	List<Meeting> allMeetings();

	String recordMeetingCost(ExpenditureItem expenditureItem);

	List<ExpenditureItem> costByMeeting(String meetingId);

	String editCost(ExpenditureItem expenditureItem);

	String deleteCost(ExpenditureItem expenditureItem);

}
