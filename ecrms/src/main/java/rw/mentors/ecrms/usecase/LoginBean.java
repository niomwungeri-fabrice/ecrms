/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Role;
import rw.mentors.ecrms.domain.Task;
import rw.mentors.ecrms.domain.User;
import rw.mentors.ecrms.exception.DataManipulationException;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.MeetingReviewService;
import rw.mentors.ecrms.service.NotificationService;
import rw.mentors.ecrms.service.SecurityService;
import rw.mentors.ecrms.service.UserService;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 15, 2017
 */
@Component
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private MeetingReviewService meetingReviewService;

	@Autowired
	private AdministrationService administrationService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ExecutionBean executionBean;

	@Autowired
	private PreMeetingTasks meetingTasks;

	private User user = new User();
	private boolean disableScheduling = true;
	private boolean disableRunningMeeting = true;
	private boolean disableSettings = true;
	private boolean disableSecurity = true;
	private boolean disableTaskAssign = true;
	private int totalNotification = 0;
	private int countPending;
	private int countApproved;
	private int countRejected;
	private int countInprogress;
	private int countSubmitted;
	private User loggedInUser;

	private String username;
	private String password;
	private String changePasswordEmail;

	public List<Meeting> myMeetings() {
		try {
			return notificationService.myMeetings(username);
		} catch (Exception e) {

		}
		return null;
	}

	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("KK:mm a");
		return outputFormat.format(date);
	}

	public String md5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte bytData[] = md.digest();
			StringBuffer hextString = new StringBuffer();
			for (int i = 0; i < bytData.length; i++) {
				String hex = Integer.toHexString(0xff & bytData[i]);
				if (hex.length() == 1) {
					hextString.append('0');
				}
				hextString.append(hex);
			}
			return hextString.toString();

		} catch (Exception e) {
			return "Error" + e.getMessage();
		}

	}

	public StreamedContent getProfilePic() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			File file = new File(getClass().getResource(Resources.FILE_PATH).getFile());
			byte[] defaultProPic = Files.readAllBytes(file.toPath());

			if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
				return new DefaultStreamedContent();
			} else {
				return new DefaultStreamedContent((new ByteArrayInputStream(defaultProPic)));
			}
		} catch (Exception e) {
			return null;
		}

	}

	public boolean disableTaskResolutionSendBtn(Execution execution) {
		if (execution.getAssignedBy() == null && execution.getAssignedTo() == null) {
			disableScheduling = true;
		}
		return disableScheduling;
	}

	public List<Meeting> todaysMeeting() {
		List<Meeting> todayMeetings = new ArrayList<Meeting>();
		for (Meeting m : myMeetings()) {
			if (DateUtils.isSameDay(m.getStartTime(), new Date())) {
				todayMeetings.add(m);
			}
		}
		return todayMeetings;
	}

	public List<Task> countTaskPending() {
		return notificationService.countTasks(EExecutionStatus.PENDING.toString());
	}

	public List<Execution> countExecutionPending() {
		return notificationService.countExecution(EExecutionStatus.PENDING.toString());
	}

	public List<Execution> executionsPerUser() {
		List<Execution> executions = new ArrayList<Execution>();
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				executions.add(execution);
			}
		}
		return executions;
	}

	public void executionsPerUsername() {
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				if (execution.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
					this.countApproved++;
				} else if (execution.getStatus().equals(EExecutionStatus.SUBMITTED.toString())) {
					this.countSubmitted++;
				} else if (execution.getStatus().equals(EExecutionStatus.PENDING.toString())) {
					this.countPending++;
				} else if (execution.getStatus().equals(EExecutionStatus.REJECTED.toString())) {
					this.countRejected++;
				} else if (execution.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())) {
					this.countInprogress++;
				}
			}
		}
	}

	public String getChangePasswordEmail() {
		return changePasswordEmail;
	}

	public void setChangePasswordEmail(String changePasswordEmail) {
		this.changePasswordEmail = changePasswordEmail;
	}

	public int countMyPendings() {
		countPending = 0;
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				if (execution.getStatus().equals(EExecutionStatus.PENDING.toString())) {
					countPending++;
				}
			}
		}
		return countPending;
	}

	public int countMyInProgress() {
		countInprogress = 0;
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				if (execution.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())) {
					countInprogress++;
				}
			}
		}
		return countInprogress;
	}

	public int countMyRejections() {
		countRejected = 0;
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				if (execution.getStatus().equals(EExecutionStatus.REJECTED.toString())) {
					countRejected++;
				}
			}
		}
		return countRejected;
	}

	public int countMyAccepted() {
		countApproved = 0;
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				if (execution.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
					countApproved++;
				}
			}
		}
		return countApproved;
	}

	public int countMySubmission() {
		countSubmitted = 0;
		for (Execution execution : meetingReviewService.allExecutions()) {
			if (execution.getAssignedTo().getEmail().equals(username)) {
				if (execution.getStatus().equals(EExecutionStatus.SUBMITTED.toString())) {
					countSubmitted++;
				}
			}
		}
		return countSubmitted;
	}

	public void updateProfile() {
		try {
			loggedInUser.setPassword(md5(loggedInUser.getPassword()));
			String msg = userService.editProfile(loggedInUser);
			successMessages("Succes", msg);
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public void uploadProPic(FileUploadEvent event) {
		try {
			UploadedFile uploadedFile = event.getFile();
			byte[] contents = uploadedFile.getContents();
			User userUpload = userService.usernameByEmail(username);
			userUpload.setProfileImage(contents);
			userUpload = userService.updateImage(userUpload);
			this.loggedInUser = userUpload;
			successMessages("Success", "Image uploaded successfully");
		} catch (Exception e) {
			errorMessages("Error:", "Selected Profile is too large");
		}
	}

	public StreamedContent getProPic() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			String email = context.getExternalContext().getRequestParameterMap().get("email");
			User user = userService.usernameByEmail(email);
			return new DefaultStreamedContent((new ByteArrayInputStream(user.getProfileImage())));
		}

	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public int getTotalNotification() {
		return totalNotification;
	}

	public void setTotalNotification(int totalNotification) {
		this.totalNotification = totalNotification;
	}

	public boolean isDisableScheduling() {
		return disableScheduling;
	}

	public void setDisableScheduling(boolean disableScheduling) {
		this.disableScheduling = disableScheduling;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String securityCode() {
		String code = "";
		try {
			int i = 6;
			String alphaNumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
			StringBuilder builder = new StringBuilder();
			Random random = new Random();
			while (i != 0) {
				char temChar[] = alphaNumeric.toCharArray();
				builder.append(temChar[random.nextInt(alphaNumeric.length())]);
				i--;
			}
			code = builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	public void sendEmail(String to, String secCode) {
		try {
			String localHostIp = InetAddress.getLocalHost().getHostAddress();
			String link = "<a href=\"http://" + localHostIp + ":81/ultima/login.xhtml?\">HERE</a>";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(
					"<h1><center>Passwored resetted successfully</center></h1><br /><br />Please change your password at first login <br />1.right corner of ther application click user profile<br />2.Click Plofile");
			stringBuilder.append("<br /><br />Login using this temporary password: <b>" + secCode + "</b>");
			stringBuilder.append("<br /><br />click " + link + " to login");

			stringBuilder.append("<br /><br /> Thanks and Regards<br />Office of the Prime Minister");
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
			message.setSubject("Password Reset");

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

	public void forgetPassword() {
		try {
			String secCode = securityCode();
			String msg = userService.forgotPassord(changePasswordEmail, md5(secCode));
			sendEmail(changePasswordEmail, secCode);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Password not changed", e.getMessage());
		}
	}

	public void createAccount() {
		try {
			String msg = "";
			Employee employee = administrationService.findEmployeeByEmail(username);
			user.setEmployee(employee);
			user.setPassword(md5(password));
			user.setUsername(username);
			for (Role role : securityService.allRoles()) {
				if (role.getName().contains("orm")) {
					user.setRoles(role);
					msg = userService.createUser(user);
					successMessages("Success", msg);
				}
			}

		} catch (Exception e) {
			errorMessages("Account not created", e.getMessage());
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login.xhtml";
	}

	public boolean isDisableRunningMeeting() {
		return disableRunningMeeting;
	}

	public boolean isDisableTaskAssign() {
		return disableTaskAssign;
	}

	public void setDisableTaskAssign(boolean disableTaskAssign) {
		this.disableTaskAssign = disableTaskAssign;
	}

	public void setDisableRunningMeeting(boolean disableRunningMeeting) {
		this.disableRunningMeeting = disableRunningMeeting;
	}

	public boolean isDisableSettings() {
		return disableSettings;
	}

	public void setDisableSettings(boolean disableSettings) {
		this.disableSettings = disableSettings;
	}

	public boolean isDisableSecurity() {
		return disableSecurity;
	}

	public void setDisableSecurity(boolean disableSecurity) {
		this.disableSecurity = disableSecurity;
	}

	public void adminAccess() {
		disableScheduling = true;
		disableRunningMeeting = true;
		disableSecurity = false;
		disableSettings = false;
		disableTaskAssign = true;
	}

	public void normalAccess() {
		disableScheduling = true;
		disableRunningMeeting = true;
		disableSecurity = true;
		disableSettings = true;
		disableTaskAssign = true;
	}

	public void dircabAcess() {
		disableTaskAssign = false;
		disableScheduling = false;
		disableRunningMeeting = false;
		disableSecurity = true;
		disableSettings = true;
	}

	public String login() {
		try {
			executionBean.setEnableExecutionPanel(false);
			meetingTasks.setEnableTaskDetailsPanel2(false);
			meetingTasks.setEnableTaskDetailsPanel(false);
			countApproved = 0;
			countRejected = 0;
			countSubmitted = 0;
			countPending = 0;
			countInprogress = 0;
			user.setUsername(username);
			user.setPassword(md5(password));
			String msg = userService.login(user);
			loggedInUser = userService.usernameByEmail(username);
			if (msg.contains("security")) {
				adminAccess();
				return msg;
			} else if (msg.contains("meeting")) {
				dircabAcess();
				return msg;
			} else if (msg.contains("dashboard")) {
				normalAccess();
				return msg;
			} else {
				errorMessages("Sign in", msg);
				return msg;
			}
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			errorMessages("Login Failed:", errorMsg);
			return errorMsg;
		}
	}

	public String profile() {
		loggedInUser = userService.usernameByEmail(username);
		return "profile.xhtml";
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
