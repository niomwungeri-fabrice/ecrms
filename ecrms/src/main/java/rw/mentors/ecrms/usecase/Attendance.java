/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.AttendanceService;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.MeetingReviewService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 26, 2017
 */
@ManagedBean
@Component
@SessionScoped
public class Attendance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private MeetingReviewService msService;

	@Autowired
	private CreatingMeetingService cmService;

	@Autowired
	private AdministrationService administrationService;

	private boolean disableParticipants = false;

	private Date today = new Date();
	private Date fromDate;
	private Date toDate;
	private String searchKey;
	private List<Meeting> meetings = new ArrayList<Meeting>();

	private Meeting selectedMeeting;
	private boolean selectAll;
	private Organization organizer;
	private String meetingId;
	private String to;
	private Institution institution = new Institution();
	private Invitation invitation = new Invitation();

	public Meeting meetingDetails() {
		this.organizer = cmService.organizerPerMeeting(meetingId);
		return cmService.findMeetingById(meetingId);
	}

	public String getMeetingObject(Meeting meeting) {
		this.selectedMeeting = meeting;
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		return "attendance";
	}

	public void delegate() {
		String msg = "";
		try {
			for (Institution institution : administrationService.instiList()) {
				if (institution.getEmail().equals(this.institution.getEmail())) {
					for (Invitation invitation : attendanceService.findInvited(meetingId)) {
						if (!invitation.getInstitution().getEmail().equals(institution.getEmail())) {
							invitation.setDelegated(true);
							msg = cmService.invite(invitation);
							successMessages("Success", msg);
						} else {
							invitation.setDelegated(true);
							msg = attendanceService.updateInvitation(invitation);
							successMessages("Success", msg);
						}
					}
				} else {
					Institution institution2 = administrationService.createExInstituion(this.institution);
					invitation.setInstitution(institution2);
					invitation.setDelegated(true);
					invitation.setMeeting(meetingDetails());
					msg = cmService.invite(invitation);
					successMessages("Success", msg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error::", e.getMessage());
		}

	}

	public void cancel() {
		try {
			String msg = "";
			for (Invitation invitation : attendanceService.findInvited(meetingId)) {
				if (invitation.getEmployee() != null) {
					if (invitation.getEmployee().getEmail().equals(to)) {
						invitation.setConfirmed(false);
						msg = attendanceService.updateInvitation(invitation);
						successMessages("Success", msg);
					}
				} else {
					if (invitation.getInstitution().getEmail().equals(to)) {
						invitation.setConfirmed(false);
						msg = attendanceService.updateInvitation(invitation);
						successMessages("Success", msg);
					}
				}
			}

		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public void confirm() {
		try {
			String msg = "";
			for (Invitation invitation : attendanceService.findInvited(meetingId)) {
				if (invitation.getEmployee() != null) {
					if (invitation.getEmployee().getEmail().equals(to)) {
						invitation.setConfirmed(true);
						msg = attendanceService.updateInvitation(invitation);
						successMessages("Success", msg);
					}
				} else {
					if (invitation.getInstitution().getEmail().equals(to)) {
						invitation.setConfirmed(true);
						msg = attendanceService.updateInvitation(invitation);
						successMessages("Success", msg);
					}
				}
			}

		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public List<Meeting> findAllMeeting() {
		return attendanceService.findAllMeetings();
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	// format dates
	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

	public Organization getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Organization organizer) {
		this.organizer = organizer;
	}

	public void searchByDate() throws ParseException {
		meetings = new ArrayList<Meeting>();
		for (Meeting meeting : attendanceService.findAllMeetings()) {
			if (meeting.getStartTime().compareTo(fromDate) >= 0 && meeting.getStartTime().compareTo(toDate) <= 0) {
				meetings.add(meeting);
			}
		}
		if (meetings.size() > 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Search Results:",
							(meetings.size() == 1) ? "" + meetings.size() + " Record is Found"
									: "" + meetings.size() + " Records are Found"));
		} else if (meetings.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Result not found:", " [ " + searchKey + " ] No matching Record!"));
		} else {
			this.meetings = new ArrayList<Meeting>();
		}
	}

	public void searchListener() {
		for (Meeting meeting : msService.allMeetings()) {
			if (meeting.getTitle().contains(searchKey)) {
				meetings.add(meeting);
				searchKey = "";
				break;
			} else {
				this.meetings = new ArrayList<Meeting>();
			}
		}
		if (meetings.size() > 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Search Results:",
							(meetings.size() == 1) ? "" + meetings.size() + " Record is Found"
									: "" + meetings.size() + " Records are Found"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Result not found:", " [ " + searchKey + " ] No matching Record!"));
		}
	}

	public void download() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String fileName = "attendanceReport.pdf";
		String contentType = "application/pdf";

		ec.responseReset();
		ec.setResponseContentType(contentType);

		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		OutputStream out = ec.getResponseOutputStream();
		Document doc = new Document();
		PdfWriter.getInstance(doc, out);
		LineSeparator ls = new LineSeparator();
		doc.open();
		Image img = Image.getInstance(Resources.LOGO);
		Paragraph header = new Paragraph();
		header.add(img);
		header.setAlignment(Image.ALIGN_CENTER);
		doc.add(header);
		doc.add(new Chunk(ls));

		doc.add(new Paragraph(Resources.HEADER));
		doc.add(new Paragraph("                                          "));
		doc.add(new Paragraph("                                          "));

		doc.add(new Paragraph("Meeting:" + selectedMeeting.getTitle()));
		doc.add(new Paragraph("Date:" + customeDateOnly(selectedMeeting.getStartTime())));

		Paragraph p = new Paragraph("Attendance Report",
				FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		doc.add(new Paragraph("                                          "));
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);
		doc.add(table);
		BaseColor color = new BaseColor(17, 113, 156);
		PdfPCell namesCell = new PdfPCell(
				new Phrase("Names", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		namesCell.setBackgroundColor(color);
		table.addCell(namesCell);

		PdfPCell assignedBy = new PdfPCell(new Phrase("Attended",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		assignedBy.setBackgroundColor(color);
		table.addCell(assignedBy);
		PdfPCell givenDate = new PdfPCell(new Phrase("Confirmed",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		givenDate.setBackgroundColor(color);
		table.addCell(givenDate);

		int countYes = 0;
		int countNo = 0;
		int countConfirmed = 0;
		int countNotConfirmed = 0;
		int countTotal = 0;

		for (Invitation m : attendanceService.findInvited(selectedMeeting.getId())) {
			countTotal++;
			if (m.getEmployee() == null) {
				table.addCell(m.getInstitution().getName());
			} else {
				table.addCell(m.getEmployee().getFirstName() + " " + m.getEmployee().getLastName());
			}

			if (m.isAttended()) {
				table.addCell("Yes");
				countYes++;
			} else {
				table.addCell(new Phrase("No",
						FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.RED)));
				countNo++;
			}

			if (m.isConfirmed()) {
				table.addCell("Yes");
				countConfirmed++;
			} else {
				table.addCell(new Phrase("No",
						FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.RED)));
				countNotConfirmed++;
			}
		}
		PdfPCell total = new PdfPCell(new Phrase("Total = " + countTotal,
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		total.setBackgroundColor(color);
		table.addCell(total);
		PdfPCell attendees = new PdfPCell(new Phrase("Yes:" + countYes + " / " + "No:" + countNo,
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		attendees.setBackgroundColor(color);
		table.addCell(attendees);
		PdfPCell confirmed = new PdfPCell(new Phrase("Yes:" + countConfirmed + " / " + "No:" + countNotConfirmed,
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		confirmed.setBackgroundColor(color);
		table.addCell(confirmed);

		doc.add(table);
		doc.add(new Paragraph("                                          "));
		doc.add(new Paragraph("                                          "));
		Paragraph printedOn = new Paragraph("Printed On:" + customeDateFull(new Date()));
		printedOn.setAlignment(Element.ALIGN_RIGHT);
		doc.add(printedOn);
		doc.close();

		fc.responseComplete();
	}

	public String customeDateOnly(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d yyyy");
		return outputFormat.format(date);
	}

	public String customeDateFull(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d yyyy HH:mm a");
		return outputFormat.format(date);
	}

	public String getInviteeDetails(Invitation invitation) {

		try {
			if (invitation.getEmployee() != null) {
				return invitation.getEmployee().getFirstName() + " " + invitation.getEmployee().getLastName();
			} else {
				return invitation.getInstitution().getName();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public String getInviteeEmail(Invitation invitation) {

		try {
			if (invitation.getEmployee() != null) {
				return invitation.getEmployee().getEmail();
			} else {
				return invitation.getInstitution().getEmail();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public void viewAgenda() {
		this.disableParticipants = true;

	}

	public List<Invitation> participants() {
		return attendanceService.findInvited(selectedMeeting.getId());
	}

	public void oneAttended(Invitation invited) {
		Invitation invitation = attendanceService.findById(invited.getId());
		if (invitation.isAttended()) {
			invitation.setAttended(false);
		} else {
			invitation.setAttended(true);
		}
		attendanceService.checkAllBoxes(invitation);
	}

	public void allAttended() {

		List<Invitation> invitation = attendanceService.findInvited(selectedMeeting.getId());
		for (Invitation invited : invitation) {
			Employee employee = invited.getEmployee();
			Meeting meeting = invited.getMeeting();
			Institution institution = invited.getInstitution();
			invited.setEmployee(employee);
			invited.setMeeting(meeting);
			invited.setInstitution(institution);
			if (!selectAll) {
				invited.setAttended(selectAll);
			} else {
				invited.setAttended(selectAll);
			}
			attendanceService.checkAllBoxes(invited);
		}

	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public boolean isDisableParticipants() {
		return disableParticipants;
	}

	public void setDisableParticipants(boolean disableParticipants) {
		this.disableParticipants = disableParticipants;
	}

	public boolean isSelectAll() {
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public void successMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void warningMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void errorMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
