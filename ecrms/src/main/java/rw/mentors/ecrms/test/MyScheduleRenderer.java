/**
 * 
 */
package rw.mentors.ecrms.test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.schedule.Schedule;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 20, 2017
 */
public class MyScheduleRenderer extends org.primefaces.component.schedule.ScheduleRenderer {
	@Override
	protected void encodeEventsAsJSON(FacesContext context, Schedule schedule, ScheduleModel model) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		Calendar calendar = Calendar.getInstance();
		writer.write("{");
		writer.write("\"events\" : [");

		if (model != null) {
			for (Iterator<ScheduleEvent> iterator = model.getEvents().iterator(); iterator.hasNext();) {
				ScheduleEvent event = iterator.next();
				calendar.setTime(event.getStartDate());
				long startDateInMillis = calendar.getTimeInMillis();

				calendar.setTime(event.getEndDate());
				long endDateInMillis = calendar.getTimeInMillis();

				writer.write("{");
				writer.write("\"id\": \"" + event.getId() + "\"");
				writer.write(",\"title\": \"" + escapeText(event.getTitle()) + "\"");
				writer.write(",\"start\": " + startDateInMillis);
				writer.write(",\"end\": " + endDateInMillis);
				writer.write(",\"allDay\":" + event.isAllDay());
				writer.write(",\"editable\":" + event.isEditable());

				if (event.getStyleClass() != null)
					writer.write(",\"className\":\"" + event.getStyleClass() + "\"");

				// This was added
				if (event instanceof MyScheduleEvent && ((MyScheduleEvent) event).getLocation() != null)
					writer.write(",\"location\":\"" + ((MyScheduleEvent) event).getLocation() + "\"");

				if (event instanceof MyScheduleEvent && ((MyScheduleEvent) event).getPurpose() != null)
					writer.write(",\"purpose\":\"" + ((MyScheduleEvent) event).getPurpose() + "\"");

				if (event instanceof MyScheduleEvent && ((MyScheduleEvent) event).getMeetingType() != null)
					writer.write(",\"meetingType\":\"" + ((MyScheduleEvent) event).getMeetingType() + "\"");
				writer.write("}");

				if (iterator.hasNext())
					writer.write(",");
			}
		}

		writer.write("]}");
	}
}
