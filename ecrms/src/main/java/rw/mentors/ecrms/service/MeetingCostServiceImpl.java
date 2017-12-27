/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.ExpenditureItemDao;
import rw.mentors.ecrms.dao.MeetingDao;
import rw.mentors.ecrms.domain.ExpenditureItem;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 13, 2017
 */
@Service
public class MeetingCostServiceImpl extends TransactionAware implements MeetingCostService {

	@Autowired
	private MeetingDao meetingDao;

	@Autowired
	private ExpenditureItemDao eiDao;

	@Override
	public List<Meeting> allMeetings() {
		return meetingDao.findAll();
	}

	@Override
	public String recordMeetingCost(ExpenditureItem expenditureItem) {
		try {
			eiDao.save(expenditureItem);
			return "Item Added Successfully";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public List<ExpenditureItem> costByMeeting(String meetingId) {
		try {
			return eiDao.costByMeeting(meetingId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String editCost(ExpenditureItem expenditureItem) {
		try {
			eiDao.update(expenditureItem);
			return "Item Updated Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

	@Override
	public String deleteCost(ExpenditureItem expenditureItem) {
		try {
			eiDao.delete(expenditureItem);
			return "Item Deleted Successfully";
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

}
