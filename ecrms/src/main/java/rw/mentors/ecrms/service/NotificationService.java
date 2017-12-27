/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Task;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 7, 2017
 */
public interface NotificationService {

	List<Meeting> myMeetings(String employeeEmail);

	List<Task> countTasks(String status);

	List<Execution> countExecution(String status);

	List<Meeting> findAllExecution();

}
