/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.Date;
import java.util.List;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.Topic;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 19, 2017
 */
public interface MeetingReviewService {

	List<Meeting> pastMeetings(Date currentDate);

	List<Meeting> upComingsMeetings(Date currentDate);

	List<Meeting> allMeetings();

	List<Meeting> myMeetings(String email);

	List<Invitation> attendees(String meetingId);

	List<Topic> topicByMeeting(String meetingId);

	List<Resolution> resolutionByTopic(String topicId);

	List<Execution> executionPerResolution(String resolutionId);

	List<Execution> allExecutions();

	List<Employee> employeesWithExecution();

	Organization organizerPerMeeting(String meetingId);

	Employee findById(String email);

	

}
