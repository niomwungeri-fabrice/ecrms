/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.EvaluationVariable;
import rw.mentors.ecrms.domain.ExecutationReport;
import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.domain.ExecutionEvaluation;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.ResourceDocument;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.TaskReportingService;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 22, 2017
 */
@Component
@ManagedBean(name = "executionBean", eager = true)
@Scope
public class ExecutionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private TaskReportingService reportingService;

	@Autowired
	private AdministrationService administrationService;

	private CreatingMeetingService cmService;

	private boolean disableMyExecutionSubmitBtn = false;

	private boolean disableRating = false;

	private boolean enableExecutionPanel = false;

	private boolean disableExecutionView = false;

	private String searchKey;

	private Execution selectedExecution = new Execution();

	private Execution selectedMyexecution = new Execution();

	private boolean disableApprovalBtn;

	private boolean disableRejectionBtn;

	private boolean disableFileUploader;

	private int myExecutionProgressBarValue;

	private ExecutationReport executationReport = new ExecutationReport();

	private Execution execution = new Execution();

	private String username;

	private ResourceDocument document = new ResourceDocument();

	private ResourceDocument uploadedDoc = new ResourceDocument();

	private ExecutionEvaluation executionEvaluation = new ExecutionEvaluation();

	private Meeting selectedMeeting;

	private List<Meeting> allMeetings;

	private boolean disableSubmitRating;

	private boolean disableChangeRating;

	private boolean skip;

	private String exeColor = "green";
	private String meetingId;

	

	public String coloringExecution(Execution execution) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
		String today = dateFormat.format(new Date());
		String deadLine = dateFormat.format(execution.getEndDate());

		Date tDate = dateFormat.parse(today);
		Date deadLin = dateFormat.parse(deadLine);
		long deff = deadLin.getTime() - tDate.getTime();
		long day = TimeUnit.DAYS.convert(deff, TimeUnit.MILLISECONDS);
		if (!execution.getStatus().toString().equals(EExecutionStatus.APPROVED.toString())) {
			if (day == 10) {
				exeColor = "yellow";
			} else if (day < 10) {
				exeColor = "red";
			} else {
				exeColor = "blue";
			}
		} else {
			exeColor = "blue";
		}
		return exeColor;
	}

	public String getMeetingObject(Meeting meeting) {
		this.selectedMeeting = meeting;
		meetingId = selectedMeeting.getId();
		return "executions";
	}

	// construct
	@PostConstruct
	public void init() {
		this.allMeetings = new ArrayList<Meeting>();

	}

	public Meeting breadCrumbMeeting() {
		return cmService.findMeetingById(meetingId);
	}

	public List<EvaluationVariable> evaluationVariables() {
		return administrationService.allEvaluationVariables();
	}

	public void rateExecution() {
		try {
			String msg = "";
			if (selectedExecution != null) {
				for (EvaluationVariable evaluation : evaluationVariables()) {
					executionEvaluation.setPoints(evaluation.getScore());
					executionEvaluation.setExecution(selectedExecution);
					executionEvaluation.setEvaluationVariable(evaluation);
					msg = administrationService.createEvaluation(executionEvaluation);
				}
				successMessages("Success", msg);
			} else {
				warningMessages("Warning", "Please Select Execution in the above table");
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public ExecutionEvaluation getExecutionEvaluation() {
		return executionEvaluation;
	}

	public void setExecutionEvaluation(ExecutionEvaluation executionEvaluation) {
		this.executionEvaluation = executionEvaluation;
	}

	public boolean isDisableSubmitRating() {
		return disableSubmitRating;
	}

	public void setDisableSubmitRating(boolean disableSubmitRating) {
		this.disableSubmitRating = disableSubmitRating;
	}

	public boolean isDisableChangeRating() {
		return disableChangeRating;
	}

	public void setDisableChangeRating(boolean disableChangeRating) {
		this.disableChangeRating = disableChangeRating;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d - KK:mm a");
		return outputFormat.format(date);
	}

	public String customNames(ExecutationReport executationReport) {

		String name = executationReport.getEmployee().getFirstName() + " "
				+ executationReport.getEmployee().getLastName().charAt(0) + ".";
		return name;
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
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void searchListener() {
		List<Meeting> all = reportingService.findAllMeetings();
		for (Meeting m : all) {
			if (m.getTitle().contains(searchKey)) {
				this.allMeetings.add(m);
				break;
			} else {
				this.allMeetings = new ArrayList<Meeting>();
			}
		}
	}

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public boolean isDisableRating() {
		return disableRating;
	}

	public void setDisableRating(boolean disableRating) {
		this.disableRating = disableRating;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public List<Meeting> getAllMeetings() {
		return allMeetings;
	}

	public void setAllMeetings(List<Meeting> allMeetings) {
		this.allMeetings = allMeetings;
	}

	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public ResourceDocument getDocument() {
		return document;
	}

	public void setDocument(ResourceDocument document) {
		this.document = document;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public ExecutationReport getExecutationReport() {
		return executationReport;
	}

	public void setExecutationReport(ExecutationReport executationReport) {
		this.executationReport = executationReport;
	}

	public ResourceDocument getUploadedDoc() {
		return uploadedDoc;
	}

	public void setUploadedDoc(ResourceDocument uploadedDoc) {
		this.uploadedDoc = uploadedDoc;
	}

	public Execution getSelectedExecution() {
		return selectedExecution;
	}

	public void setSelectedExecution(Execution selectedExecution) {
		this.selectedExecution = selectedExecution;
	}

	public boolean isDisableApprovalBtn() {
		return disableApprovalBtn;
	}

	public void setDisableApprovalBtn(boolean disableApprovalBtn) {
		this.disableApprovalBtn = disableApprovalBtn;
	}

	public boolean isDisableRejectionBtn() {
		return disableRejectionBtn;
	}

	public void setDisableRejectionBtn(boolean disableRejectionBtn) {
		this.disableRejectionBtn = disableRejectionBtn;
	}

	public boolean isDisableFileUploader() {
		return disableFileUploader;
	}

	public void setDisableFileUploader(boolean disableFileUploader) {
		this.disableFileUploader = disableFileUploader;
	}

	public int getMyExecutionProgressBarValue() {
		return myExecutionProgressBarValue;
	}

	public void setMyExecutionProgressBarValue(int myExecutionProgressBarValue) {
		this.myExecutionProgressBarValue = myExecutionProgressBarValue;
	}

	public Execution getSelectedMyexecution() {
		return selectedMyexecution;
	}

	public void setSelectedMyexecution(Execution selectedMyexecution) {
		this.selectedMyexecution = selectedMyexecution;
	}

	public boolean isDisableMyExecutionSubmitBtn() {
		return disableMyExecutionSubmitBtn;
	}

	public void setDisableMyExecutionSubmitBtn(boolean disableMyExecutionSubmitBtn) {
		this.disableMyExecutionSubmitBtn = disableMyExecutionSubmitBtn;
	}

	public boolean isEnableExecutionPanel() {
		return enableExecutionPanel;
	}

	public void setEnableExecutionPanel(boolean enableExecutionPanel) {
		this.enableExecutionPanel = enableExecutionPanel;
	}

	public boolean isDisableExecutionView() {
		return disableExecutionView;
	}

	public void setDisableExecutionView(boolean disableExecutionView) {
		this.disableExecutionView = disableExecutionView;
	}

	public List<Execution> executionByResolution() {

		return reportingService.executionByResolution(meetingId);
	}

	public void getExecutionObject(Execution execution) {
		this.execution = execution;
	}

	public void viewExecutions() {

		this.disableExecutionView = true;
	}

	public List<Execution> executionAssignedToBy() {
		List<Execution> execu = reportingService.executionByResolution(meetingId);
		List<Execution> execuNew = new ArrayList<Execution>();
		for (Execution e : execu) {
			if (e.getAssignedBy().getEmail().equals(username)) {
				execuNew.add(e);
			}
		}
		execuNew.sort(Comparator.comparing(Execution::getGivenDate).reversed());
		return execuNew;
	}

	public List<Execution> executionAssignedToMe() {
		List<Execution> execu = reportingService.executionByResolution(meetingId);
		List<Execution> execuNew = new ArrayList<Execution>();
		for (Execution e : execu) {
			if (e.getAssignedTo().getEmail().equals(this.username)) {
				execuNew.add(e);
			}
		}
		execuNew.sort(Comparator.comparing(Execution::getGivenDate).reversed());
		return execuNew;
	}

	public Meeting myMeetingFromURL() {
		return cmService.findMeetingById(meetingId);
	}

	public void myExecutionChart() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String fileName = "myExecutionChart.pdf";
		String contentType = "application/pdf";

		ec.responseReset();
		ec.setResponseContentType(contentType);

		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

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

		doc.add(new Paragraph("Meeting:" + selectedMeeting.getTitle()));
		doc.add(new Paragraph("Date:" + customeDateOnly(selectedMeeting.getStartTime())));

		Paragraph p = new Paragraph("Given Execution Report",
				FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		Employee emp = reportingService.findByEmail(username);
		doc.add(new Paragraph("Employee Names:" + emp.getFirstName() + " " + emp.getLastName()));
		doc.add(new Paragraph("                                          "));
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);
		doc.add(table);

		BaseColor color = new BaseColor(17, 113, 156);

		PdfPCell assignedBy = new PdfPCell(new Phrase("Comment By",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		assignedBy.setBackgroundColor(color);
		table.addCell(assignedBy);
		PdfPCell givenDate = new PdfPCell(new Phrase("Date and Time",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		givenDate.setBackgroundColor(color);
		table.addCell(givenDate);
		PdfPCell namesCell = new PdfPCell(new Phrase("Comment",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		namesCell.setBackgroundColor(color);
		table.addCell(namesCell);

		for (ExecutationReport e : executionReports()) {
			table.addCell(e.getEmployee().getFirstName() + " " + e.getEmployee().getLastName());
			table.addCell(convertToTime(e.getCommentDate()));
			table.addCell((e.getDescription()));
		}
		doc.add(table);
		doc.add(new Paragraph("                                          "));
		doc.add(new Paragraph("                                          "));
		Paragraph printedOn = new Paragraph("Printed On:" + customeDateFull(new Date()));
		printedOn.setAlignment(Element.ALIGN_RIGHT);
		doc.add(printedOn);
		doc.close();

		fc.responseComplete();

	}

	public void myExecutionReport() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String fileName = "myExecutionReport.pdf";
		String contentType = "application/pdf";

		ec.responseReset();
		ec.setResponseContentType(contentType);

		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

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

		doc.add(new Paragraph("Meeting:" + selectedMeeting.getTitle()));
		doc.add(new Paragraph("Date:" + customeDateOnly(selectedMeeting.getStartTime())));

		Paragraph p = new Paragraph("Given Execution Report",
				FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		Employee emp = reportingService.findByEmail(username);
		doc.add(new Paragraph("Employee Names:" + emp.getFirstName() + " " + emp.getLastName()));
		doc.add(new Paragraph("                                          "));
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		doc.add(table);
		BaseColor color = new BaseColor(17, 113, 156);
		PdfPCell namesCell = new PdfPCell(
				new Phrase("Names", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		namesCell.setBackgroundColor(color);
		table.addCell(namesCell);
		PdfPCell assignedBy = new PdfPCell(new Phrase("Assigned By",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		assignedBy.setBackgroundColor(color);
		table.addCell(assignedBy);
		PdfPCell givenDate = new PdfPCell(new Phrase("Given Date",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		givenDate.setBackgroundColor(color);
		table.addCell(givenDate);
		PdfPCell deadline = new PdfPCell(new Phrase("DeadLine",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		deadline.setBackgroundColor(color);
		table.addCell(deadline);
		PdfPCell status = new PdfPCell(
				new Phrase("Status", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		status.setBackgroundColor(color);
		table.addCell(status);

		for (Execution e : executionAssignedToMe()) {
			table.addCell(e.getResolution().getDescription());
			table.addCell(e.getAssignedBy().getFirstName() + " " + e.getAssignedBy().getLastName());
			table.addCell(customeDateOnly(e.getGivenDate()));
			table.addCell(customeDateOnly(e.getEndDate()));
			table.addCell(e.getStatus());
		}
		doc.add(table);
		doc.add(new Paragraph("                                          "));
		doc.add(new Paragraph("                                          "));
		Paragraph printedOn = new Paragraph("Printed On:" + customeDateFull(new Date()));
		printedOn.setAlignment(Element.ALIGN_RIGHT);
		doc.add(printedOn);
		doc.close();

		fc.responseComplete();

	}

	public void executionToFollowUp() throws Exception {

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String fileName = "executionToFollowUp.pdf";
		String contentType = "application/pdf";

		ec.responseReset();
		ec.setResponseContentType(contentType);

		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

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

		doc.add(new Paragraph("Meeting:" + selectedMeeting.getTitle()));
		doc.add(new Paragraph("Date:" + customeDateOnly(selectedMeeting.getStartTime())));

		Paragraph p = new Paragraph("Execution Report",
				FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		Employee emp = reportingService.findByEmail(username);
		doc.add(new Paragraph("Employee Names:" + emp.getFirstName() + " " + emp.getLastName()));
		doc.add(new Paragraph("                                          "));
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		doc.add(table);
		BaseColor color = new BaseColor(17, 113, 156);
		PdfPCell namesCell = new PdfPCell(
				new Phrase("Names", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		namesCell.setBackgroundColor(color);
		table.addCell(namesCell);
		PdfPCell assignedBy = new PdfPCell(new Phrase("Assigned To",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		assignedBy.setBackgroundColor(color);
		table.addCell(assignedBy);
		PdfPCell givenDate = new PdfPCell(new Phrase("Given Date",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		givenDate.setBackgroundColor(color);
		table.addCell(givenDate);
		PdfPCell deadline = new PdfPCell(new Phrase("DeadLine",
				FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		deadline.setBackgroundColor(color);
		table.addCell(deadline);
		PdfPCell status = new PdfPCell(
				new Phrase("Status", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
		status.setBackgroundColor(color);
		table.addCell(status);

		for (Execution e : executionAssignedToBy()) {
			table.addCell(e.getResolution().getDescription());
			table.addCell(e.getAssignedTo().getFirstName() + " " + e.getAssignedTo().getLastName());
			table.addCell(customeDateOnly(e.getGivenDate()));
			table.addCell(customeDateOnly(e.getEndDate()));
			table.addCell(e.getStatus());
		}
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

	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}

	public EExecutionStatus[] allExecutionStatus() {
		return EExecutionStatus.values();
	}

	public void executionApproval() {
		try {
			if (!this.selectedExecution.getStatus().equals(EExecutionStatus.APPROVED.toString())) {

				String msg = reportingService.executionAproval(executationReport, selectedExecution, uploadedDoc,
						username);
				successMessages("Success", msg);
				executationReport = new ExecutationReport();
				uploadedDoc = new ResourceDocument();
			} else {
				successMessages("Execution", "Already Approved ");
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public List<ExecutationReport> executionReports() {
		List<ExecutationReport> executationReports = reportingService.reportsByExecution(this.execution.getId());
		executationReports.sort(Comparator.comparing(ExecutationReport::getCommentDate).reversed());
		return executationReports;
	}

	public List<ResourceDocument> resourcePerExecutionReport(ExecutationReport executationReport) {
		return reportingService.resourcePerExecutionReport(executationReport.getId());
	}

	public List<ResourceDocument> executionAttachements() {
		return reportingService.findExecutionAttachments(selectedMyexecution.getId());
	}

	public List<ExecutationReport> downloadExecutionFiles(Execution execution) {
		return reportingService.reportsByExecution(execution.getId());
	}

	public void executionRejection() {
		try {
			if (!this.selectedExecution.getStatus().equals(EExecutionStatus.REJECTED.toString())) {
				String msg = reportingService.executionRejection(executationReport, selectedExecution, uploadedDoc,
						username);
				successMessages("Success", msg);
				executationReport = new ExecutationReport();
				uploadedDoc = new ResourceDocument();
			} else {
				successMessages("Execution", "Already Approved ");
			}
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void upload(FileUploadEvent event) {
		try {
			UploadedFile uploadedFile = event.getFile();
			String fileName = uploadedFile.getFileName();
			String contentType = uploadedFile.getContentType();
			byte[] contents = uploadedFile.getContents();
			this.document.setDocumentName(contentType);
			document.setFileContent(contents);
			document.setFileType(contentType);
			document.setFileName(fileName);
			uploadedDoc = reportingService.fileSaver(document);
			successMessages("Success", "File(s) uploaded successfully");
			document = new ResourceDocument();
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void executionReporting() {
		try {
			String msg = reportingService.executionReporting(selectedMyexecution, executationReport, uploadedDoc,
					username);
			successMessages("Execution", msg);
			this.executationReport = new ExecutationReport();
			this.uploadedDoc = new ResourceDocument();
			this.enableExecutionPanel = true;
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void openExecution(Execution execution) {
		try {

			if (execution.getStatus().equals(EExecutionStatus.PENDING.toString())
					|| execution.getStatus().equals(EExecutionStatus.REJECTED.toString())
					|| execution.getStatus().equals(EExecutionStatus.UPDATED.toString())) {
				this.enableExecutionPanel = true;
				this.selectedExecution = execution;
				this.disableApprovalBtn = true;
				this.disableRejectionBtn = true;
				this.disableFileUploader = true;
				this.myExecutionProgressBarValue = 0;
				this.disableRating = true;
				this.disableChangeRating = true;
				this.disableSubmitRating = true;
			} else if (execution.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())) {
				this.selectedExecution = execution;
				this.enableExecutionPanel = true;
				this.disableApprovalBtn = true;
				this.disableRejectionBtn = true;
				this.disableApprovalBtn = true;
				this.myExecutionProgressBarValue = 50;
				this.disableRating = true;
				this.disableChangeRating = true;
				this.disableSubmitRating = true;
			} else if (execution.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
				this.selectedExecution = execution;
				this.enableExecutionPanel = true;
				this.disableApprovalBtn = true;
				this.disableRejectionBtn = true;
				this.disableFileUploader = true;
				this.myExecutionProgressBarValue = 100;
				this.disableRating = true;
				this.disableChangeRating = true;
				this.disableSubmitRating = true;
			} else if (execution.getStatus().equals(EExecutionStatus.SUBMITTED.toString())) {
				this.selectedExecution = execution;
				this.enableExecutionPanel = true;
				this.disableApprovalBtn = false;
				this.disableRejectionBtn = false;
				this.disableFileUploader = false;
				this.myExecutionProgressBarValue = 75;
				this.disableRating = false;
				this.disableChangeRating = false;
				this.disableSubmitRating = false;
			} else {
				this.selectedExecution = execution;
				this.enableExecutionPanel = false;
				this.myExecutionProgressBarValue = 100;
				this.disableRating = true;
				this.disableChangeRating = true;
				this.disableSubmitRating = true;
			}

		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}

	}

	
	public void openMyExecution(Execution execution) {
		try {

			if (execution.getStatus().equals(EExecutionStatus.PENDING.toString())
					|| execution.getStatus().equals(EExecutionStatus.REJECTED.toString())
					|| execution.getStatus().equals(EExecutionStatus.UPDATED.toString())) {
				selectedMyexecution.setStatus(EExecutionStatus.IN_PROGROCESS.toString());
				this.selectedMyexecution = execution;
				selectedMyexecution.setStatus(EExecutionStatus.IN_PROGROCESS.toString());
				String msg = reportingService.updateExecution(selectedMyexecution);
				successMessages("Success", msg);
				this.enableExecutionPanel = true;
				this.disableFileUploader = false;
				this.disableMyExecutionSubmitBtn = false;
			} else if (execution.getStatus().equals(EExecutionStatus.IN_PROGROCESS.toString())) {
				this.selectedMyexecution = execution;
				this.enableExecutionPanel = true;
				this.disableMyExecutionSubmitBtn = false;
				this.disableFileUploader = false;
			} else if (execution.getStatus().equals(EExecutionStatus.APPROVED.toString())) {
				this.selectedMyexecution = execution;
				this.enableExecutionPanel = true;
				this.disableMyExecutionSubmitBtn = true;
				this.disableFileUploader = true;
			} else if (execution.getStatus().equals(EExecutionStatus.SUBMITTED.toString())) {
				this.selectedMyexecution = execution;
				this.enableExecutionPanel = true;
				this.disableApprovalBtn = false;
				this.disableMyExecutionSubmitBtn = true;
				this.disableFileUploader = true;
			} else {
				this.selectedMyexecution = execution;
				this.enableExecutionPanel = false;
			}
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
