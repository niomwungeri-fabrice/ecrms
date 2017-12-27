/**
 *
 */
package rw.mentors.ecrms.usecase;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
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
import rw.mentors.ecrms.domain.MeetingType;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Presentation;
import rw.mentors.ecrms.domain.Presenter;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.exception.DataManipulationException;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.test.DefaultScheduleEvent;
import rw.mentors.ecrms.test.MyScheduleEvent;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 9, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class CreateMeeting implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CreatingMeetingService cmService;

	@Autowired
	private AdministrationService admService;

	private Date today = new Date();

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	private String username;
	private String orginizer;
	private String presenterType = "Internal";
	private String presenter;
	private String presenterEmp;
	private String presenterInt;
	private Meeting selectedMeeting;
	private String selectedType;
	private Date startTime;
	private Date endTime;
	private boolean disablePresenter = true;
	private boolean disableMeetingDetails = false;
	private boolean disablePrintShare = true;
	private List<Employee> localInvitedEmployee;
	private String keyWord;
	private String selectedSector;
	private Meeting meeting = new Meeting();
	private Meeting newMeeting = new Meeting();
	private Topic topic = new Topic();
	private Invitation invitation = new Invitation();
	private Institution institution = new Institution();
	private boolean noPresenter;
	private boolean noPresenterEdit;
	private String presenterTypeEdit = "Internal";

	private Organization organizer;

	private boolean disableEmployeePresenter;

	public void createExternalEntity() {
		String msg = "";
		try {
			msg = admService.createInstitution(institution);
			institution = new Institution();
			successMessages("Success", msg);
			allInstitutions = cmService.findAllInstutions();
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error", e.getMessage());
		}
	}

	public void getMeetingObj(Meeting meeting) {
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		this.meeting = meeting;
	}

	public boolean isNoPresenterEdit() {
		return noPresenterEdit;
	}

	public void setNoPresenterEdit(boolean noPresenterEdit) {
		this.noPresenterEdit = noPresenterEdit;
	}

	public String getMeetingObject(Meeting meeting) {
		this.meeting = meeting;
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		return "agenda";
	}

	public List<Invitation> findInviteesByMeeting() {
		return cmService.invitationByMeeting(meeting.getId());
	}

	public String getPresenterTypeEdit() {
		return presenterTypeEdit;
	}

	public void setPresenterTypeEdit(String presenterTypeEdit) {
		this.presenterTypeEdit = presenterTypeEdit;
	}

	public boolean isDisableEmployeePresenter() {
		return disableEmployeePresenter;
	}

	public void setDisableEmployeePresenter(boolean disableEmployeePresenter) {
		this.disableEmployeePresenter = disableEmployeePresenter;
	}

	public String getOrginizer() {
		return orginizer;
	}

	public void setOrginizer(String orginizer) {
		this.orginizer = orginizer;
	}

	public String getPresenterEmp() {
		return presenterEmp;
	}

	public void setPresenterEmp(String presenterEmp) {
		this.presenterEmp = presenterEmp;
	}

	public String getPresenterInt() {
		return presenterInt;
	}

	public void setPresenterInt(String presenterInt) {
		this.presenterInt = presenterInt;
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

	public void topicsReport() {
		try {
			Meeting m = cmService.findMeetingById(meeting.getId());
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			String fileName = "topics.pdf";
			String contentType = "application/pdf";

			ec.responseReset();
			ec.setResponseContentType(contentType);

			ec.setResponseHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

			OutputStream out = ec.getResponseOutputStream();
			Document doc = new Document(PageSize.A4.rotate());
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

			doc.add(new Paragraph("Meeting:" + m.getTitle()));
			doc.add(new Paragraph("Date:" + customeDateOnly(m.getStartTime())));

			Paragraph p = new Paragraph("Meeting Agenda",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			doc.add(table);
			BaseColor color = new BaseColor(17, 113, 156);
			PdfPCell namesCell = new PdfPCell(new Phrase("Time",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			namesCell.setBackgroundColor(color);
			table.addCell(namesCell);
			PdfPCell assignedBy = new PdfPCell(new Phrase("Topic Name",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			assignedBy.setBackgroundColor(color);
			table.addCell(assignedBy);
			PdfPCell objective = new PdfPCell(new Phrase("Objective",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			objective.setBackgroundColor(color);
			table.addCell(objective);
			PdfPCell givenDate = new PdfPCell(new Phrase("Presenter",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			givenDate.setBackgroundColor(color);
			table.addCell(givenDate);

			for (Topic t : tbyMeeting()) {
				table.addCell(convertToEndTime(t.getStartTime()) + " - " + convertToEndTime(t.getEndTime()));
				table.addCell(t.getName());
				table.addCell(t.getObjective());
				table.addCell(getPresenterDetails(t));

			}
			doc.add(table);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));
			Paragraph printedOn = new Paragraph("Printed On:" + customeDateFull(new Date()));
			printedOn.setAlignment(Element.ALIGN_RIGHT);
			doc.add(printedOn);
			doc.close();

			fc.responseComplete();

		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
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

	public void presenterActivator() {
		this.disablePresenter = false;

	}

	// get topic object
	public void getTopicObj(Topic topic) {
		this.topic = topic;
	}

	public String getPresenterDetails(Topic topic) {

		try {
			if (topic.getPresenter().getEmployee() != null) {
				return topic.getPresenter().getEmployee().getFirstName() + " "
						+ topic.getPresenter().getEmployee().getLastName();
			} else {
				return topic.getPresenter().getInstitution().getName();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public String customEmail(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("EEEEE MMM d yyyy HH:mm a");
		return outputFormat.format(date);
	}

	public String customeDateForEmail(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("HH:mm a");
		return outputFormat.format(date);
	}

	public void sendEmail(String to) {
		try {
			String localHostIp = InetAddress.getLocalHost().getHostAddress();
			String link = "<a href=\"http://" + localHostIp + ":81/ultima/confirmattendance.xhtml?meetingId="
					+ meeting.getId() + "&to=" + to + "\">HERE</a>";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"<h1 style=font-weight: bold; color:maroon;><center>INVITATION</center></h1></center><br />");
			stringBuilder.append("Office of the Prime Minister would like to invite you inthe meeting that will take place ");
			stringBuilder.append(customEmail(meeting.getStartTime()));
			stringBuilder.append(" at ");
			stringBuilder.append(meeting.getLocation());
			stringBuilder.append("<br /><br />Meeting Purpose :");
			stringBuilder.append(meeting.getPurpose());
			stringBuilder.append("<br /><br />Time:");
			stringBuilder.append(customeDateForEmail(meeting.getStartTime()));
			stringBuilder.append(" - ");
			stringBuilder.append(customeDateForEmail(meeting.getEndTime()));
			stringBuilder.append(
					"<br /><br />Your presence would be appreciated click " + link + " to confirm your attendance");

			stringBuilder.append("<br /><br /> Thanks and Regards");
			String emailMessage = stringBuilder.toString();
			String from = "nyf2k16@gmail.com";// change accordingly
			final String username = from;// change accordingly
			final String password = "CIERRY12";// change accordingly

			// Assuming you are sending email through relay.jangosmtp.net
			String host = "smtp.gmail.com";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");

			// Get the Session object.
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("INVITATION");

			// Now set the actual message

			message.setContent(emailMessage, "text/html");
			// Send message
			Transport.send(message);

		} catch (MessagingException e) {
			throw new DataManipulationException(e.getMessage());
		} catch (java.net.UnknownHostException e) {
			throw new DataManipulationException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendEmailEmployee(String to) {
		try {
			String localHostIp = InetAddress.getLocalHost().getHostAddress();
			String link = "<a href=\"http://" + localHostIp + ":81/ultima/dashboard.xhtml?meetingId="
					+ meeting.getId() + "&to=" + to + "\">HERE</a>";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"<h1 style=font-weight: bold; color:maroon;><center>INVITATION</center></h1></center><br />");
			stringBuilder.append("Office of the prime minister would like to invite you in the meeting that will take place ");
			stringBuilder.append(customEmail(meeting.getStartTime()));
			stringBuilder.append(" at ");
			stringBuilder.append(meeting.getLocation());
			stringBuilder.append("<br /><br />Meeting Purpose :");
			stringBuilder.append(meeting.getPurpose());
			stringBuilder.append("<br /><br />Time:");
			stringBuilder.append(customeDateForEmail(meeting.getStartTime()));
			stringBuilder.append(" - ");
			stringBuilder.append(customeDateForEmail(meeting.getEndTime()));
			stringBuilder.append(
					"<br /><br />Your presence would be appreciated click " + link + " to confirm your attendance");

			stringBuilder.append("<br /><br /> Thanks and Regards");
			String emailMessage = stringBuilder.toString();
			String from = "nyf2k16@gmail.com";// change accordingly
			final String username = from;// change accordingly
			final String password = "CIERRY12";// change accordingly

			// Assuming you are sending email through relay.jangosmtp.net
			String host = "smtp.gmail.com";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");

			// Get the Session object.
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("INVITATION");

			// Now set the actual message

			message.setContent(emailMessage, "text/html");
			// Send message
			Transport.send(message);

		} catch (MessagingException e) {
			throw new DataManipulationException(e.getMessage());
		} catch (java.net.UnknownHostException e) {
			throw new DataManipulationException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// invite
	public void invite() {
		String msg = "";
		try {

			invitation.setMeeting(meeting);
			for (Employee employee : selectedEmployees) {
				sendEmailEmployee(employee.getEmail());
				invitation.setEmployee(employee);
				invitation.setInstitution(null);
				msg = cmService.invite(invitation);
				selectedEmployees = new ArrayList<Employee>();
			}
			for (Institution institution : selectedInstitutions) {
				sendEmail(institution.getEmail());
				invitation.setInstitution(institution);
				invitation.setEmployee(null);
				msg = cmService.invite(invitation);
				selectedInstitutions = new ArrayList<Institution>();
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning", e.getMessage()));
		}
	}

	public List<String> completeEmployee(String query) {
		List<String> results = new ArrayList<String>();
		List<Employee> emp = admService.findAll();
		for (Employee e : emp) {
			if (e.getEmail().contains(query) || e.getFirstName().contains(query) || e.getLastName().contains(query)) {
				results.add(e.getEmail());
			}
		}

		return results;

	}

	// auto complete invitation
	public List<String> completeEmployees(String query) {
		List<String> results = new ArrayList<String>();
		List<Employee> emp = admService.findAll();
		for (Employee e : emp) {
			if (e.getEmail().contains(query) || e.getFirstName().contains(query) || e.getLastName().contains(query)) {
				results.add(e.getEmail());
			}
		}

		return results;

	}

	public List<String> completeInstitutions(String query) {
		List<String> results = new ArrayList<String>();
		List<Institution> insta = admService.instiList();
		for (Institution i : insta) {
			if (i.getEmail().contains(query) || i.getName().contains(query)) {
				results.add(i.getEmail());
			}
		}
		return results;

	}

	// find all meeting types
	public List<MeetingType> findMType() {
		return cmService.findAllMeetingType();
	}

	// find all meetings
	public List<Meeting> findMeetings() {
		return cmService.findAllMeetings();
	}

	// delete topic
	public void removeTopic(Topic topic) {
		try {
			String msg = cmService.removeTopic(topic);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error:" + e.getMessage(), "Error:" + e.getMessage()));
		}
	}

	public void onTabChange(TabChangeEvent event) {
		this.presenterType = event.getTab().getTitle();
		// System.out.println("Presenter Type::::" + this.presenterType);
	}

	public void onTabChangeEdit(TabChangeEvent event) {
		this.presenterTypeEdit = event.getTab().getTitle();
		// System.out.println("Presenter Type Edit::::" + this.presenterTypeEdit);
	}

	public void addTopicTest() {
		successMessages("Success", "Value " + noPresenter);
	}

	// add new topic
	public void addTopic() {
		try {
			String msg = "";
			Presentation presentation = new Presentation();
			Presenter presenter = new Presenter();
			if (!noPresenter) {
				if (presenterType.equals("Internal")) {
					if (this.presenterEmp == "") {
						warningMessages("Warning", "No Presenter?, Check Box ");

					} else {
						Employee employee = cmService.finByEmail(this.presenterEmp);
						presenter.setEmployee(employee);
						presenter.setInstitution(null);
						topic.setPresenter(presenter);
						presentation.setPresenter(presenter);
						cmService.createPresentation(presentation);
						this.presenterEmp = new String();
						topic.setMeeting(meeting);
						msg = cmService.addTopic(topic);
						this.disablePrintShare = false;
						successMessages("Success", msg);
						topic = new Topic();
					}
				} else {
					if (this.presenterInt == "") {
						warningMessages("Warning", "No Presenter?, Check Box ");

					} else {
						Institution institution = cmService.findByEmail(this.presenterInt);
						presenter.setEmployee(null);
						presenter.setInstitution(institution);
						topic.setPresenter(presenter);
						presentation.setPresenter(presenter);
						cmService.createPresentation(presentation);
						this.presenterInt = new String();
						topic.setMeeting(meeting);
						msg = cmService.addTopic(topic);
						this.disablePrintShare = false;
						successMessages("Success", msg);
						topic = new Topic();
					}
				}
			} else {
				topic.setMeeting(meeting);
				msg = cmService.addTopic(topic);
				this.presenterEmp = new String();
				this.presenterEmp = new String();
				this.disablePrintShare = false;
				successMessages("Success", msg);
				topic = new Topic();
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error:", e.getMessage());

		}
	}

	// edit topic
	public void editTopic(Topic t) {
		try {
			Presenter presenter = null;
			Presentation presentation = null;
			String msg = "";
			if (!noPresenterEdit) {
				if (presenterTypeEdit.equals("Internal")) {
					if (this.presenterEmp == "") {
						errorMessages("Warning", "Check no prenter box or select presenter");
					} else {
						Employee employee = cmService.finByEmail(this.presenterEmp);
						if (t.getPresenter() != null) {
							presenter = cmService.presenterById(t.getPresenter().getId());
							presentation = cmService.presentationByPresenter(presenter.getId());
							presenter.setEmployee(employee);
							presenter.setInstitution(null);
							t.setPresenter(presenter);
							presentation.setPresenter(presenter);
							cmService.updatePresentation(presentation);
							msg = cmService.editTopic(t);
							this.presenterEmp = new String();
							successMessages("Success", msg);
						} else {
							Presentation presentationn = new Presentation();
							Presenter presenterr = new Presenter();
							presenterr.setEmployee(employee);
							presenterr.setInstitution(null);
							t.setPresenter(presenterr);
							presentationn.setPresenter(presenterr);
							cmService.createPresentation(presentationn);
							this.presenterEmp = new String();
							t.setMeeting(meeting);
							msg = cmService.editTopic(t);
							successMessages("Success", msg);
						}
					}
				} else {
					if (this.presenterInt == "") {
						errorMessages("Warning", "Check no prenter box or selectpresenter >>insta");
					} else {
						Institution institution = cmService.findByEmail(this.presenterInt);
						if (t.getPresenter() != null) {
							presenter = cmService.presenterById(t.getPresenter().getId());
							presentation = cmService.presentationByPresenter(presenter.getId());
							presenter.setEmployee(null);
							presenter.setInstitution(institution);
							t.setPresenter(presenter);
							presentation.setPresenter(presenter);
							cmService.updatePresentation(presentation);
							msg = cmService.editTopic(t);
							this.presenterInt = new String();
							successMessages("Success", msg);
						} else {
							Presentation presentationn = new Presentation();
							Presenter presenterr = new Presenter();
							presenterr.setEmployee(null);
							presenterr.setInstitution(institution);
							t.setPresenter(presenterr);
							presentationn.setPresenter(presenterr);
							cmService.createPresentation(presentationn);
							this.presenterInt = new String();
							t.setMeeting(meeting);
							msg = cmService.editTopic(t);
							successMessages("Success", msg);
						}
					}
				}
			} else {
				msg = cmService.editTopic(t);
				successMessages("Success", msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error", e.getMessage());
		}
	}

	// topic by meeting
	public List<Topic> tbyMeeting() {
		List<Topic> topics = cmService.topicsByMeetingList(this.meeting.getId());
		if (!topics.isEmpty()) {
			disablePrintShare = false;
		} else {
			disablePrintShare = true;
		}
		topics.sort(Comparator.comparing(Topic::getStartTime));
		return topics;
	}

	public Date convertToDate(long milliSeconds) {
		return new Date(milliSeconds);
	}

	// convert from millisecond to time
	public String fromMillToTime(long milli) {
		Date date = new Date(milli);
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
		return outputFormat.format(date);
	}

	// convert from time to millisecond
	public long convertToMilliSeconds(Date timeInString) {
		Date d = new Date(timeInString.getTime());
		return d.getTime();
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	// formated Date
	public String convertToEndTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
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

	// get meeting year
	public String meetingYear(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("yyyy");
		return outputFormat.format(date);
	}

	public boolean isNoPresenter() {
		return noPresenter;
	}

	public void setNoPresenter(boolean noPresenter) {
		this.noPresenter = noPresenter;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public Meeting getNewMeeting() {
		return newMeeting;
	}

	public Organization orginier() {
		return this.organizer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Organization getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Organization organizer) {
		this.organizer = organizer;
	}

	public void setNewMeeting(Meeting newMeeting) {
		this.newMeeting = newMeeting;
	}

	public List<Employee> getLocalInvitedEmployee() {
		return localInvitedEmployee;
	}

	public void setLocalInvitedEmployee(List<Employee> localInvitedEmployee) {
		this.localInvitedEmployee = localInvitedEmployee;
	}

	public boolean isDisablePrintShare() {
		return disablePrintShare;
	}

	public void setDisablePrintShare(boolean disablePrintShare) {
		this.disablePrintShare = disablePrintShare;
	}

	public boolean isDisableMeetingDetails() {
		return disableMeetingDetails;
	}

	public void setDisableMeetingDetails(boolean disableMeetingDetails) {
		this.disableMeetingDetails = disableMeetingDetails;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getSelectedSector() {
		return selectedSector;
	}

	public void setSelectedSector(String selectedSector) {
		this.selectedSector = selectedSector;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	/**
	 * CheckBoxMenu test start
	 */

	@PostConstruct
	public void init() {
		allEmployees = new ArrayList<Employee>();
		allInstitutions = new ArrayList<Institution>();
		for (Employee employee : cmService.findAllEmployees()) {
			allEmployees.add(employee);
		}
		for (Institution institution : cmService.findAllInstutions()) {
			allInstitutions.add(institution);
		}

	}

	private List<Employee> selectedEmployees;
	private List<Employee> allEmployees;

	private List<Institution> selectedInstitutions;
	private List<Institution> allInstitutions;

	public List<Institution> getAllInstitutions() {
		return allInstitutions;
	}

	public void setAllInstitutions(List<Institution> allInstitutions) {
		this.allInstitutions = allInstitutions;
	}

	public List<Employee> getAllEmployees() {
		return allEmployees;
	}

	public void setAllEmployees(List<Employee> allEmployees) {
		this.allEmployees = allEmployees;
	}

	public Invitation getInvitation() {
		return invitation;
	}

	public void setInvitation(Invitation invitation) {
		this.invitation = invitation;
	}

	public List<Institution> getSelectedInstitutions() {
		return selectedInstitutions;
	}

	public void setSelectedInstitutions(List<Institution> selectedInstitutions) {
		this.selectedInstitutions = selectedInstitutions;
	}

	public List<Employee> getSelectedEmployees() {
		return selectedEmployees;
	}

	public void setSelectedEmployees(List<Employee> selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}

	// end
	// ================Testing Calendar==========================
	private ScheduleModel eventModel;

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public Employee getEmployee(String id) {
		if (id == null) {
			throw new IllegalArgumentException("no id provided");
		}
		for (Employee invitee : allEmployees) {
			if (id.equals(invitee.getId())) {
				return invitee;
			}
		}
		return null;
	}

	public Institution getInstitution(String id) {
		if (id == null) {
			throw new IllegalArgumentException("no id provided");
		}
		for (Institution invitee : allInstitutions) {
			if (id.equals(invitee.getId())) {
				return invitee;
			}
		}
		return null;
	}

	public ScheduleModel fillSechedule() {
		ScheduleModel eventModel = new DefaultScheduleModel();
		for (Meeting m : cmService.findAllMeetings()) {
			eventModel.addEvent(new DefaultScheduleEvent(m.getTitle(), m.getStartTime(), m.getEndTime(), m.getPurpose(),
					m.getLocation(), m.getMeetingType()));
		}
		return eventModel;
	}

	private MyScheduleEvent event = new DefaultScheduleEvent();
	private MyScheduleEvent editEvent = new DefaultScheduleEvent();

	public MyScheduleEvent getEditEvent() {
		return editEvent;
	}

	public void setEditEvent(MyScheduleEvent editEvent) {
		this.editEvent = editEvent;
	}

	public MyScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(MyScheduleEvent event) {
		this.event = event;
	}

	public void editMeetingDetails() {
		try {
			if (selectedType != "") {
				MeetingType m = cmService.findByMtType(selectedType);
				meeting.setMeetingType(m);
			}
			String msg = cmService.editMeetingInfor(meeting, orginizer);
			successMessages("Success", msg);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error:", e.getMessage());
		}
	}

	public void testUpdatetopic() {
		successMessages("Success", "topic updated Success");
	}

	public void addEvent(ActionEvent actionEvent) {
		try {
			MeetingType m = cmService.findByMtType(selectedType);
			meeting.setMeetingType(m);
			meeting.setStartTime(event.getStartDate());
			meeting.setEndTime(event.getEndDate());
			meeting.setTitle(event.getTitle().trim());
			meeting.setPurpose(event.getPurpose().trim());
			meeting.setLocation(event.getLocation().trim());
			Employee employee = admService.findEmployeeByEmail(username);
			meeting.setEmployee(employee);
			String res = cmService.createMeeting(meeting, orginizer);
			this.disableMeetingDetails = true;
			successMessages("Success", res);
			this.organizer = cmService.organizerPerMeeting(meeting.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages("Error", e.getMessage());

		}
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	public boolean isDisablePresenter() {
		return disablePresenter;
	}

	public void setDisablePresenter(boolean disablePresenter) {
		this.disablePresenter = disablePresenter;
	}

	public String getPresenterType() {
		return presenterType;
	}

	public void setPresenterType(String presenterType) {
		this.presenterType = presenterType;
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