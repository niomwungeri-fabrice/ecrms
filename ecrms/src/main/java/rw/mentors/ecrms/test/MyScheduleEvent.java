/**
 * 
 */
package rw.mentors.ecrms.test;

import rw.mentors.ecrms.domain.MeetingType;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 20, 2017
 */
public interface MyScheduleEvent extends org.primefaces.model.ScheduleEvent {
	String getLocation();

	String getPurpose();

	MeetingType getMeetingType();
}
