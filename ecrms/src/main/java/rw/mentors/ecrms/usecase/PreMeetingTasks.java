/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.TaskReport;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.TaskReportingService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 29, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class PreMeetingTasks implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private CreatingMeetingService creatingMeetingService;

	@Autowired
	private TaskReportingService reportingService;

	@Autowired
	private AdministrationService administrationService;

	private boolean disableSubmitBtn = false;
	private boolean disableDelegateBtn = true;
	private boolean disableApprovalBtn = true;
	private boolean disableRejectionBtn = true;

	private boolean toggeableMeetingPanel = false;
	private boolean enableTaskDetailsPanel = false;
	private boolean enableTaskDetailsPanel2 = false;
	private boolean disableFileUploader = false;
	private boolean disableTaskTabView = false;

	private Task task = new Task();
	private Date today = new Date();

	private StreamedContent file;
	private Meeting selectedMeeting;
	private Task selectedTask = new Task();
	private String assignedBy;
	private String assignedTo;
	private Task selectedTask2;
	private ResourceDocument document = new ResourceDocument();
	private ResourceDocument uploadedDoc = new ResourceDocument();
	private String selectedExecutionStatus;

	private List<Employee> assignToFilted;

	private Date fromDate;
	private Date toDate;
	private String searchKey;
	private String username;
	private String meetingId;
	private List<Meeting> meetings = new ArrayList<Meeting>();

	public String getMeetingObject(Meeting meeting) {
		this.selectedMeeting = meeting;
		meetingId = selectedMeeting.getId();
		return "tasks";
	}

	public void searchByDate() throws ParseException {
		meetings = new ArrayList<Meeting>();
		for (Meeting meeting : creatingMeetingService.findAllMeetings()) {
			if (meeting.getStartTime().compareTo(fromDate) >= 0 && meeting.getStartTime().compareTo(toDate) <= 0) {
				meetings.add(meeting);
				System.err.println("Test:" + meeting.getStartTime());
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
		for (Meeting meeting : creatingMeetingService.findAllMeetings()) {
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

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
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

	public void startDownload(ResourceDocument resourceD) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			externalContext.setResponseHeader("Content-Type", resourceD.getFileType());
			externalContext.setResponseHeader("Content-Length", resourceD.getFileContent().length + "");
			externalContext.setResponseHeader("Content-Disposition",
					"attachment;filename=\"" + resourceD.getFileName() + "\"");
			externalContext.getResponseOutputStream().write(resourceD.getFileContent());
			facesContext.responseComplete();
			successMessages("Success", "Download Completed>>" + resourceD.getFileName());
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public PreMeetingTasks() {
		assignToFilted = new ArrayList<Employee>();
		this.disableTaskTabView = false;
		this.enableTaskDetailsPanel = false;
		this.enableTaskDetailsPanel2 = false;
	}

	public List<Employee> getAssignToFilted() {
		return assignToFilted;
	}

	public void setAssignToFilted(List<Employee> assignToFilted) {
		this.assignToFilted = assignToFilted;
	}

	public String getSelectedExecutionStatus() {
		return selectedExecutionStatus;
	}

	public void setSelectedExecutionStatus(String selectedExecutionStatus) {
		this.selectedExecutionStatus = selectedExecutionStatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ResourceDocument getUploadedDoc() {
		return uploadedDoc;
	}

	public boolean isDisableFileUploader() {
		return disableFileUploader;
	}

	public void setDisableFileUploader(boolean disableFileUploader) {
		this.disableFileUploader = disableFileUploader;
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

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public boolean isDisableDelegateBtn() {
		return disableDelegateBtn;
	}

	public void setDisableDelegateBtn(boolean disableDelegateBtn) {
		this.disableDelegateBtn = disableDelegateBtn;
	}

	public boolean isDisableRejectionBtn() {
		return disableRejectionBtn;
	}

	public void setDisableRejectionBtn(boolean disableRejectionBtn) {
		this.disableRejectionBtn = disableRejectionBtn;
	}

	public boolean isDisableApprovalBtn() {
		return disableApprovalBtn;
	}

	public void setDisableApprovalBtn(boolean disableApprovalBtn) {
		this.disableApprovalBtn = disableApprovalBtn;
	}

	public boolean isDisableSubmitBtn() {
		return disableSubmitBtn;
	}

	public void setDisableSubmitBtn(boolean disableSubmitBtn) {
		this.disableSubmitBtn = disableSubmitBtn;
	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public boolean isDisableTaskTabView() {
		return disableTaskTabView;
	}

	public void setDisableTaskTabView(boolean disableTaskTabView) {
		this.disableTaskTabView = disableTaskTabView;
	}

	public boolean isToggeableMeetingPanel() {
		return toggeableMeetingPanel;
	}

	public void setToggeableMeetingPanel(boolean toggeableMeetingPanel) {
		this.toggeableMeetingPanel = toggeableMeetingPanel;
	}

	public boolean isEnableTaskDetailsPanel() {
		return enableTaskDetailsPanel;
	}

	public void setEnableTaskDetailsPanel(boolean enableTaskDetailsPanel) {
		this.enableTaskDetailsPanel = enableTaskDetailsPanel;
	}

	public Task getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
	}

	public boolean isEnableTaskDetailsPanel2() {
		return enableTaskDetailsPanel2;
	}

	public void setEnableTaskDetailsPanel2(boolean enableTaskDetailsPanel2) {
		this.enableTaskDetailsPanel2 = enableTaskDetailsPanel2;
	}

	public Task getSelectedTask2() {
		return selectedTask2;
	}

	public void setSelectedTask2(Task selectedTask2) {
		this.selectedTask2 = selectedTask2;
	}

	public EExecutionStatus[] allExecutionStatus() {
		return EExecutionStatus.values();
	}

	public void openTask(Task task) {
		try {
			if (task.getStatus().equals(EExecutionStatus.PENDING.toString())
					|| task.getStatus().equals(EExecutionStatus.REJECTED.toString())) {

				task.setStatus(EExecutionStatus.IN_PROGROCESS.toString());
				String msg = reportingService.updateTask(task);
				successMessages("Task", msg);
				this.selectedTask = task;
				this.enableTaskDetailsPanel = true;
				this.disableSubmitBtn = false;
				this.disableDelegateBtn = false;
			} else if (task.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())) {
				this.selectedTask = task;
				this.enableTaskDetailsPanel = true;
				this.disableSubmitBtn = false;
				this.disableDelegateBtn = false;
				this.disableFileUploader = false;
			} else if (task.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
				this.selectedTask = task;
				this.enableTaskDetailsPanel = true;
				this.disableSubmitBtn = true;
				this.disableDelegateBtn = true;
				this.disableFileUploader = true;
			} else {
				this.selectedTask = task;
				this.disableSubmitBtn = false;
				this.enableTaskDetailsPanel = true;
				this.disableSubmitBtn = true;
				this.disableDelegateBtn = true;
				this.disableFileUploader = true;
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void openCreatedTask(Task task) {
		try {
			if (task.getStatus().equals(EExecutionStatus.PENDING.toString())
					|| task.getStatus().equals(EExecutionStatus.REJECTED.toString())
					|| task.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())
					|| task.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
				this.selectedTask = task;
				this.enableTaskDetailsPanel2 = true;
				this.disableApprovalBtn = true;
				this.disableRejectionBtn = true;
				this.disableFileUploader = true;

			} else if (task.getStatus().equals(EExecutionStatus.SUBMITTED.toString())) {
				this.selectedTask = task;
				this.enableTaskDetailsPanel2 = true;
				this.disableApprovalBtn = false;
				this.disableRejectionBtn = false;
				this.disableFileUploader = false;
			} else {
				this.enableTaskDetailsPanel2 = true;
				this.disableApprovalBtn = true;
				this.disableRejectionBtn = true;
				this.disableRejectionBtn = false;
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public String fromMeeting() {
		this.toggeableMeetingPanel = true;
		this.disableTaskTabView = true;
		return "tasks";
	}

	public List<Task> taskByMeeting() {
		return creatingMeetingService.taskByMeeting(meetingId);
	}

	public List<Task> assignedByMe() {
		List<Task> tB = taskByMeeting();
		List<Task> assignedTaskList = new ArrayList<Task>();
		for (Task t : tB) {
			if (t.getAssignedBy().getEmail().equals(username)) {
				assignedTaskList.add(t);
			}
		}
		return assignedTaskList;
	}

	public List<Task> assignedToMe() {
		List<Task> tB = taskByMeeting();
		List<Task> assignedTaskList = new ArrayList<Task>();
		for (Task t : tB) {
			if (t.getAssignedTo().getEmail().equals(username)) {
				assignedTaskList.add(t);
			}
		}
		return assignedTaskList;
	}

	public void viewTasks() {

		this.disableTaskTabView = true;
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

	public void sendEmail(String to, String description, Date dueDate, String status) {
		try {
			String localHostIp = InetAddress.getLocalHost().getHostAddress();
			String link = "<a href=\"http://" + localHostIp + ":81/ultima/tasks.xhtml?meetingId=" + meetingId
					+ "&to=" + to + "\">HERE</a>";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"<h1 style=font-weight: bold; color: maroon;><center>Task Assignment</center></h1></center><br />");
			stringBuilder.append("Hello ," + employeeDetails(to) + "<br />");
			stringBuilder
					.append("You have been assigned the following pre - Meeting Task in e-Confirence:<br /><br />");
			stringBuilder.append("<b>Decriptions:</b>&nbsp;&nbsp;" + description + "<br />");
			stringBuilder.append("<b>Due date:</b>&nbsp;&nbsp;" + convertToTimeForEmail(dueDate) + "<br />");
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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createTask() {
		try {
			String msg = creatingMeetingService.createTask(task, assignedBy, assignedTo, meetingId, uploadedDoc);
			if (assignedBy != "" && assignedTo != "") {
				sendEmail(assignedTo, task.getDescription(), task.getDeadLine(), task.getStatus());
				sendEmail(assignedBy, task.getDescription(), task.getDeadLine(), task.getStatus());
			}
			successMessages("Success", msg);
			task = new Task();
			uploadedDoc = new ResourceDocument();
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}

	}

	public List<Meeting> allMeetings() {
		return creatingMeetingService.findAllMeetings();
	}

	public void onAssigned() {
		System.out.println(assignedBy);
	}

	public List<String> completeEmployeeAssignTo(String query) {
		List<String> results = new ArrayList<String>();
		List<Invitation> inv = creatingMeetingService.assigness(assignedBy);
		for (Invitation e : inv) {
			if (e.getEmployee().getEmail().contains(query) || e.getEmployee().getFirstName().contains(query)
					|| e.getEmployee().getLastName().contains(query)) {
				results.add(e.getEmployee().getEmail());
			}
		}
		return results;
	}

	private TaskReport taskReport = new TaskReport();

	public TaskReport getTaskReport() {
		return taskReport;
	}

	public void setTaskReport(TaskReport taskReport) {
		this.taskReport = taskReport;
	}

	public void tastReporting() {
		try {
			String msg = reportingService.taskReporting(selectedTask, taskReport, uploadedDoc);
			successMessages("Task", msg);
			taskReport = new TaskReport();
			uploadedDoc = new ResourceDocument();
			this.enableTaskDetailsPanel = true;
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void editTaskCreated() {
		successMessages("Success", "Task Edited");
	}

	public void taskApproval() {
		try {
			if (!this.selectedTask.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
				String msg = reportingService.taskAproval(taskReport, selectedTask, uploadedDoc);
				successMessages("Success", msg);
				taskReport = new TaskReport();
				uploadedDoc = new ResourceDocument();
				this.enableTaskDetailsPanel = true;
			} else {
				successMessages("Task", "Already Approved ");
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void taskRejection() {
		try {
			if (!this.selectedTask.getStatus().equals(EExecutionStatus.REJECTED.toString())) {
				String msg = reportingService.taskRejection(taskReport, selectedTask, uploadedDoc);
				successMessages("Success", msg);
				taskReport = new TaskReport();
				uploadedDoc = new ResourceDocument();
				this.enableTaskDetailsPanel = true;
			} else {
				successMessages("Task", "Already Rejected ");
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public List<TaskReport> downloadTaskFiles(Task task) {
		return reportingService.reportsByTask(task.getId());
	}

	public void getTaskObject(Task task) {
		this.task = task;
	}

	public List<TaskReport> taskReports() {
		return reportingService.reportsByTask(task.getId());
	}

	public List<ResourceDocument> resourcePerTaskReport(TaskReport taskReport) {
		return reportingService.resourcePerTaskReport(taskReport.getId());
	}

	public List<ResourceDocument> taskAttachements() {
		return reportingService.findTaskAttachments(selectedTask.getId());
	}

	public void deleteTaskCreated() {
		successMessages("Success", "Task Deleted");
	}

	public List<String> completeEmployee(String query) {
		List<String> results = new ArrayList<String>();
		List<Employee> emp = reportingService.findAllEmployee();
		for (Employee e : emp) {
			if (e.getEmail().contains(query) || e.getFirstName().contains(query) || e.getLastName().contains(query)) {
				results.add(e.getEmail());
			}
		}
		return results;
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
		return reportingService.findAllEmployee();
	}

	public void upload(FileUploadEvent event) {
		try {
			UploadedFile uploadedFile = event.getFile();
			String fileName = uploadedFile.getFileName();
			String contentType = uploadedFile.getContentType();
			byte[] contents = uploadedFile.getContents();
			document.setDocumentName(contentType);
			document.setFileContent(contents);
			document.setFileType(contentType);
			document.setFileName(fileName);
			uploadedDoc = creatingMeetingService.fileSaver(document);
			successMessages("Success", "File(s) uploaded successfully");
			document = new ResourceDocument();
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
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
