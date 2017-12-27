/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.EConclusionType;
import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.ExecutationReport;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.domain.Resolution;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Topic;
import rw.mentors.ecrms.exception.DataManipulationException;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.MeetingMinuteService;
import rw.mentors.ecrms.service.TaskReportingService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 24, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class MeetingMinutes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private CreatingMeetingService cmService;

	@Autowired
	private MeetingMinuteService mmService;

	@Autowired
	private AdministrationService administrationService;

	@Autowired
	private TaskReportingService reportingService;

	private Organization organizer;
	private Meeting selectedMeeting;
	private boolean disableMeetingDetails = false;
	private String selectedCType;
	private Resolution resolution = new Resolution();
	private Topic topic = new Topic();
	private UploadedFile file;
	private Execution execution = new Execution();
	private String assignedTo = "";
	private String assignedBy = "";
	private String presenter;
	private String selectedPresenter;
	private double weight;
	private String username;
	private List<Employee> assignToFilted;

	private Date fromDate;
	private Date toDate;
	private String searchKey;
	private List<Meeting> meetings = new ArrayList<Meeting>();
	private ExecutationReport executationReport = new ExecutationReport();

	public void getCurrentExecution(Execution execution) {
		this.execution = execution;
	}

	public void getCurrentExecution(Execution execution, Resolution resolution) {
		this.execution = execution;
		this.resolution = resolution;
	}

	public void updateResolution(Resolution resolution) {
		try {
			resolution.setTopic(topic);
			resolution.setType(selectedCType);
			String msg = mmService.editResolution(resolution, execution, assignedBy, assignedTo, uploadedDoc);
			resolution = new Resolution();
			uploadedDoc = new ResourceDocument();
			assignedTo = "";
			assignedBy = "";
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void addComment() {
		try {
			execution.setStatus(EExecutionStatus.UPDATED.toString());
			executationReport.setExecution(execution);
			reportingService.executionUpdated(executationReport, execution, uploadedDoc, username);
			successMessages("Success", "Comment added succussfully");
			executationReport = new ExecutationReport();
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public boolean disableComment(Execution execution) {
		if (execution.getStatus().toString().equals(EExecutionStatus.PENDING.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean disableEdit(Execution execution) {
		if (!execution.getStatus().toString().equals(EExecutionStatus.PENDING.toString())
				|| execution.getAssignedTo().getEmail().equals(username)) {
			return true;
		} else {
			return false;
		}
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	public String convertToTimeForExecution(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM dd yy");
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

	public String getMeetingObject(Meeting meeting) {
		this.selectedMeeting = meeting;
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		return "meetingmunites";
	}

	public ExecutationReport getExecutationReport() {
		return executationReport;
	}

	public void setExecutationReport(ExecutationReport executationReport) {
		this.executationReport = executationReport;
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

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
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

	public void getResolutionObject(Resolution resolution) {
		this.resolution = resolution;
	}

	public MeetingMinutes() {
		assignToFilted = new ArrayList<Employee>();
	}

	public List<Employee> getAssignToFilted() {
		return assignToFilted;
	}

	public void setAssignToFilted(List<Employee> assignToFilted) {
		this.assignToFilted = assignToFilted;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	private Date today = new Date();

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public String getSelectedPresenter() {
		return selectedPresenter;
	}

	public void setSelectedPresenter(String selectedPresenter) {
		this.selectedPresenter = selectedPresenter;
	}

	public String getPresenter() {
		return presenter;
	}

	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public String getSelectedCType() {
		return selectedCType;
	}

	public void setSelectedCType(String selectedCType) {
		this.selectedCType = selectedCType;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void setDisableMeetingDetails(boolean disableMeetingDetails) {
		this.disableMeetingDetails = disableMeetingDetails;
	}

	public boolean isDisableMeetingDetails() {
		return disableMeetingDetails;
	}

	private ResourceDocument document = new ResourceDocument();
	private ResourceDocument uploadedDoc = new ResourceDocument();

	public ResourceDocument getUploadedDoc() {
		return uploadedDoc;
	}

	public void setUploadedDoc(ResourceDocument uploadedDoc) {
		this.uploadedDoc = uploadedDoc;
	}

	public ResourceDocument getDocument() {
		return document;
	}

	public void setDocument(ResourceDocument document) {
		this.document = document;
	}

	// format dates
	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}

	public void onByChange() {
		for (Employee e : autoCompleteEmployee()) {
			if (!e.getEmail().equals(assignedBy)) {
				assignToFilted.add(e);
			}
		}
	}

	public void removeAllEmployee() {
		assignToFilted.removeAll(assignToFilted);
	}

	public List<Employee> autoCompleteEmployee() {
		return mmService.findAllEmployee();
	}

	public void upload(FileUploadEvent event) {
		try {
			UploadedFile uploadedFile = event.getFile();
			String fileName = uploadedFile.getFileName();
			String contentType = uploadedFile.getContentType();
			byte[] contents = uploadedFile.getContents();
			String type = FilenameUtils.getExtension(uploadedFile.toString());
			document.setDocumentName(contentType);
			document.setFileContent(contents);
			document.setFileType(type);
			document.setFileName(fileName);
			uploadedDoc = mmService.fileSaver(document);
			successMessages("Success", "File Uploaded Successfully");
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public List<Meeting> findAllMeeting() {
		return mmService.findAllMeetings();
	}

	public List<String> completeEmployee(String query) {
		List<String> results = new ArrayList<String>();
		List<Employee> emp = mmService.findAllEmployee();
		for (Employee e : emp) {
			if (e.getEmail().contains(query) || e.getFirstName().contains(query) || e.getLastName().contains(query)) {
				results.add(e.getEmail());
			}
		}
		return results;
	}

	public List<Resolution> getResolutions() {
		List<Resolution> res = mmService.resolutionPerTopic(this.topic.getId());
		return res;
	}

	public List<Execution> executionByResolution(Resolution resolution) {
		return resolution.getExecution();
	}

	public void getTopicObj(Topic topic) {
		this.topic = topic;
	}

	public String employeeDetails(String email) {
		Employee employee = administrationService.findEmployeeByEmail(email);
		return employee.getFirstName();
	}

	public String convertToTimeForEmail(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("EEEEE MMM d yy");
		return outputFormat.format(date);
	}

	public void sendEmail(String to, String description, Date dueDate, int weight, String status) {
		try {
			String localHostIp = InetAddress.getLocalHost().getHostAddress();
			String link = "<a href=\"http://" + localHostIp + ":8070/ultima/executions.xhtml?meetingId="
					+ selectedMeeting.getId() + "&to=" + to + "\">HERE</a>";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"<h1 style=font-weight: bold; color: maroon;><center>Task Assignment</center></h1></center><br />");
			stringBuilder.append("Hello ," + employeeDetails(to) + "<br />");
			stringBuilder.append("You have been assigned the following Executions in e-Conference:<br /><br />");
			stringBuilder.append("<b>Decriptions:</b>&nbsp;&nbsp;" + description + "<br />");
			stringBuilder.append("<b>Due date:</b>&nbsp;&nbsp;" + convertToTimeForEmail(dueDate) + "<br />");
			stringBuilder.append("<b>Weight:</b>&nbsp;&nbsp;" + weight + "<br />");
			stringBuilder.append("<b>Status:</b>&nbsp;&nbsp;" + status + "<br /><br />");

			stringBuilder.append("Clicking " + link + " will take you to this Task in e-Conference:");

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
			message.setSubject("Task assignment");

			// Now set the actual message

			message.setContent(emailMessage, "text/html");
			// Send message
			Transport.send(message);

		} catch (javax.mail.MessagingException e) {
			throw new DataManipulationException("No internet Connection");
		} catch (java.net.UnknownHostException e) {
			throw new DataManipulationException("No internet Connection");
		}
	}

	public void addResolution() {
		try {
			resolution.setTopic(topic);
			resolution.setType(selectedCType);
			String msg = mmService.createResolution(resolution, execution, assignedBy, assignedTo, uploadedDoc);
			resolution = new Resolution();
			uploadedDoc = new ResourceDocument();
			assignedTo = "";
			assignedBy = "";
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public void deleteResolution(Resolution resolution) {
		try {
			String msg = mmService.deleteResolution(resolution);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	private Invitation invitation = new Invitation();

	public void invite(Execution execution) {
		try {
			List<Invitation> invitations = cmService.invitationByMeeting(selectedMeeting.getId());
			for (Invitation invitation : invitations) {
				if (invitation.getEmployee() != null) {
					if (!invitation.getEmployee().getEmail().equals(execution.getAssignedBy().getEmail())) {
						this.invitation.setMeeting(selectedMeeting);
						this.invitation.setInstitution(null);
						this.invitation.setEmployee(execution.getAssignedBy());
						cmService.invite(this.invitation);
					}
					if (!invitation.getEmployee().getEmail().equals(execution.getAssignedTo().getEmail())) {
						this.invitation.setMeeting(selectedMeeting);
						this.invitation.setInstitution(null);
						this.invitation.setEmployee(execution.getAssignedTo());
						cmService.invite(this.invitation);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	public void sendPerExecution(Execution e) {
		try {
			if (e.getStatus().toString().equals(EExecutionStatus.PENDING.toString())) {

				sendEmail(e.getAssignedBy().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
						e.getWeight(), e.getStatus());
				sendEmail(e.getAssignedTo().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
						e.getWeight(), e.getStatus());

				reportingService.updateExecution(e);
				invite(e);
				successMessages("Success", "Email sent successfully");
			} else {
				e.setStatus(EExecutionStatus.UPDATED.toString());

				sendEmail(e.getAssignedBy().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
						e.getWeight(), e.getStatus());
				sendEmail(e.getAssignedTo().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
						e.getWeight(), e.getStatus());
				executationReport.setExecution(e);
				executationReport.setDescription(e.getResolution().getDescription());
				reportingService.updateExecution(e);
				reportingService.executionUpdated(executationReport, e, uploadedDoc, username);
				successMessages("Success", "Email sent successfully");
			}
		} catch (Exception e2) {
			errorMessages("Connection Error", e2.getMessage());
		}
	}

	public void sendExecution() {
		try {
			for (Resolution r : mmService.resolutionPerTopic(this.topic.getId())) {
				for (Execution e : r.getExecution()) {
					if (e.getStatus().toString().equals(EExecutionStatus.PENDING.toString())) {
						sendEmail(e.getAssignedBy().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
								e.getWeight(), e.getStatus());
						sendEmail(e.getAssignedTo().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
								e.getWeight(), e.getStatus());
						reportingService.updateExecution(e);
						invite(e);
					} else {
						e.setStatus(EExecutionStatus.UPDATED.toString());
						sendEmail(e.getAssignedBy().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
								e.getWeight(), e.getStatus());
						sendEmail(e.getAssignedTo().getEmail(), e.getResolution().getDescription(), e.getEndDate(),
								e.getWeight(), e.getStatus());
						executationReport.setExecution(e);
						executationReport.setDescription(e.getResolution().getDescription());
						reportingService.updateExecution(e);
						reportingService.executionUpdated(executationReport, e, uploadedDoc, username);
					}

				}
			}
			successMessages("Success", "Email sent successfully");
		} catch (Exception e) {
			errorMessages("Connection Error", e.getMessage());
		}
	}

	public EConclusionType[] getTypes() {
		return EConclusionType.values();
	}

	public void viewAgenda() {
		this.disableMeetingDetails = true;
		this.organizer = cmService.organizerPerMeeting(selectedMeeting.getId());

	}

	// topic by meeting
	public List<Topic> tbyMeeting() {
		try {
			List<Topic> topics = mmService.topicsByMeetingList(selectedMeeting.getId());
			if (!topics.isEmpty()) {
				disableMeetingDetails = false;
			} else {
				disableMeetingDetails = true;
			}
			return topics;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Resolution> resolutionPerTopic(Topic topic) {
		return mmService.resolutionPerTopic(topic.getId());
	}

	public List<Execution> allExecution() {
		return mmService.findAllExecution();
	}

	public List<String> completeEmployeee(String query) {
		List<String> results = new ArrayList<String>();
		List<Employee> emp = mmService.findAllEmployee();
		List<Institution> insta = mmService.findAllInsta();
		for (Employee e : emp) {
			if (e.getEmail().contains(query) || e.getFirstName().contains(query) || e.getLastName().contains(query)) {
				results.add(e.getEmail());
			}
		}
		for (Institution i : insta) {
			if (i.getEmail().contains(query) || i.getName().contains(query)) {
				results.add(i.getEmail());
			}
		}

		return results;
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
