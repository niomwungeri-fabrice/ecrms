/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Task;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 7, 2017
 */
public interface StatisticService {
	List<Meeting> allMeetings();

	List<Execution> executionPerResolution(String meetingId);

	List<Execution> findAllExecutions();

	Meeting findMeetingById(String id);

	List<Task> taskByMeeting(String meetingId);

	List<Employee> findAllEmployee();
}
