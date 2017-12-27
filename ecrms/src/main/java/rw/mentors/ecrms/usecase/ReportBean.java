package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.MeetingReviewService;

@Component
@ManagedBean
@ViewScoped
public class ReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private CreatingMeetingService cmService;

	@Autowired
	private MeetingReviewService msService;

	private List<Meeting> filteredMeetings;

	public List<Meeting> meetings() {
		return cmService.findAllMeetings();
	}

	public List<Meeting> getFilteredMeetings() {
		return filteredMeetings;
	}

	public void setFilteredMeetings(List<Meeting> filteredMeetings) {
		this.filteredMeetings = filteredMeetings;
	}

	public List<Topic> tbyMeeting(Meeting meeting) {
		List<Topic> topics = cmService.topicsByMeetingList(meeting.getId());
		topics.sort(Comparator.comparing(Topic::getStartTime));
		return topics;
	}

	public List<Execution> getResoTasks(Topic topic) {
		List<Execution> res = new ArrayList<Execution>();
		for (Resolution r : msService.resolutionByTopic(topic.getId())) {
			for (Execution ex : msService.executionPerResolution(r.getId())) {
				res.add(ex);
			}
		}
		return res;
	}
}
