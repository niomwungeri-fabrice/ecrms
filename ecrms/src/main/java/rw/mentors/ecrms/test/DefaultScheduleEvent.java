/**
 * 
 */
package rw.mentors.ecrms.test;

import java.io.Serializable;
import java.util.Date;

import rw.mentors.ecrms.domain.MeetingType;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 20, 2017
 */
public class DefaultScheduleEvent extends Object implements MyScheduleEvent, Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String title;

	private Date startDate;

	private Date endDate;

	private boolean allDay = true;

	private String styleClass;

	private Object data;

	private String purpose;

	private String location;

	private MeetingType meetingType;

	private boolean editable = true;

	private String description;

	private String url;

	public DefaultScheduleEvent() {
	}

	public DefaultScheduleEvent(String title, Date start, Date end) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
	}

	public DefaultScheduleEvent(String title, Date start, Date end, boolean allDay) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.allDay = allDay;
	}

	public DefaultScheduleEvent(String title, Date start, Date end, String styleClass) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.styleClass = styleClass;
	}

	public DefaultScheduleEvent(String title, Date start, Date end, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.data = data;
	}

	public DefaultScheduleEvent(String title, Date startDate, Date endDate, String purpose, String location,
			MeetingType meetingType) {
		super();
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.purpose = purpose;
		this.location = location;
		this.meetingType = meetingType;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public String getStyleClass() {
		return styleClass;
	}

	@Override
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public String getPurpose() {
		return purpose;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public MeetingType getMeetingType() {
		return meetingType;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getUrl() {
		return url;
	}

}
