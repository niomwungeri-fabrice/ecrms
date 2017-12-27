/**
 * 
 */
package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Topic;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 24, 2017
 */
public interface MeetingMinuteService {

	Meeting meetingById(String uuid);

	// meeting
	List<Meeting> findAllMeetings();

	// topic
	List<Topic> topicsByMeetingList(String meeting);

	// resolution
	List<Resolution> resolutionPerTopic(String topic);

	String createResolution(Resolution resolution, Execution execution, String assignedBy, String assignedTo,
			ResourceDocument document);

	String editResolution(Resolution resolution, Execution execution, String assignedBy, String assignedTo,
			ResourceDocument document);

	String deleteResolution(Resolution resolution);

	// employee
	List<Employee> findAllEmployee();

	Employee findByEmail(String email);

	// execution
	List<Execution> findAllExecution();

	// institutions
	List<Institution> findAllInsta();

	// files
	ResourceDocument fileSaver(ResourceDocument resourceDocument);

}
