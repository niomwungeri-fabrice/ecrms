/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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

import rw.mentors.ecrms.domain.ExpenditureItem;
import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.domain.Organization;
import rw.mentors.ecrms.service.CreatingMeetingService;
import rw.mentors.ecrms.service.MeetingCostService;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 13, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class MeetingCostBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Autowired
	private MeetingCostService meetingCostService;

	@Autowired
	private CreatingMeetingService cmService;

	private Meeting selectedMeeting;
	private ExpenditureItem expenditureItem = new ExpenditureItem();
	private boolean enableCostPanel = false;
	private Organization organizer;

	private Date fromDate;
	private Date toDate;
	private String searchKey;
	private String meetingId;
	private List<Meeting> meetings = new ArrayList<Meeting>();

	public String getMeetingObject(Meeting meeting) {
		this.selectedMeeting = meeting;
		this.organizer = cmService.organizerPerMeeting(meeting.getId());
		meetingId = meeting.getId();
		return "meetingcost";
	}

	
	public Meeting getSelectedMeeting() {
		return selectedMeeting;
	}

	public void setSelectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
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

	public void getExpenditure(ExpenditureItem expenditureItem) {
		this.expenditureItem = expenditureItem;
	}

	public void deleteItem(ExpenditureItem expenditureItem) {
		try {
			String msg = meetingCostService.deleteCost(expenditureItem);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
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

	// format dates
	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}

	public void updateItems(ExpenditureItem expenditureItem) {
		try {
			expenditureItem.setTotalCost(expenditureItem.getQuantity().multiply(expenditureItem.getUnitCost()));
			String msg = meetingCostService.editCost(expenditureItem);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void meetingCostReport() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			String fileName = "meetingCost.pdf";
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

			Paragraph p = new Paragraph("Meeting Cost Report",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			doc.add(table);
			BaseColor color = new BaseColor(17, 113, 156);
			PdfPCell namesCell = new PdfPCell(new Phrase("Item",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			namesCell.setBackgroundColor(color);
			table.addCell(namesCell);
			PdfPCell assignedBy = new PdfPCell(new Phrase("Unit Cost(Rwf)",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			assignedBy.setBackgroundColor(color);
			table.addCell(assignedBy);
			PdfPCell givenDate = new PdfPCell(new Phrase("Quantity",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			givenDate.setBackgroundColor(color);
			table.addCell(givenDate);
			PdfPCell totalCost = new PdfPCell(new Phrase("Total Cost(Rwf)",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			totalCost.setBackgroundColor(color);
			table.addCell(totalCost);

			for (ExpenditureItem ei : costByMeeting()) {
				table.addCell(ei.getItemName());
				table.addCell(ei.getUnitCost() + "");
				table.addCell(ei.getQuantity() + "");
				table.addCell((ei.getQuantity().multiply(ei.getUnitCost())) + " Rwf");

			}
			PdfPCell grandTotal = new PdfPCell(new Phrase("G.Total",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			grandTotal.setBackgroundColor(color);
			table.addCell(grandTotal);

			PdfPCell unitCost = new PdfPCell(
					new Phrase("", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			unitCost.setBackgroundColor(color);
			table.addCell(unitCost);

			PdfPCell quantity = new PdfPCell(
					new Phrase("", FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			quantity.setBackgroundColor(color);
			table.addCell(quantity);

			PdfPCell grandTotol = new PdfPCell(new Phrase(calculateGrandTotal() + " Rwf",
					FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD, BaseColor.WHITE)));
			grandTotol.setBackgroundColor(color);

			table.addCell(grandTotol);

			doc.add(table);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));
			Paragraph printedOn = new Paragraph("Printed On:" + customeDateFull(new Date()));
			printedOn.setAlignment(Element.ALIGN_RIGHT);
			doc.add(printedOn);
			doc.close();

			fc.responseComplete();

		} catch (Exception ex) {
			System.err.println("Error:" + ex.getMessage());
			errorMessages("Error:", ex.getMessage());
		}
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

	public Organization getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Organization organizer) {
		this.organizer = organizer;
	}

	public ExpenditureItem getExpenditureItem() {
		return expenditureItem;
	}

	public void setExpenditureItem(ExpenditureItem expenditureItem) {
		this.expenditureItem = expenditureItem;
	}

	public boolean isEnableCostPanel() {
		return enableCostPanel;
	}

	public void setEnableCostPanel(boolean enableCostPanel) {
		this.enableCostPanel = enableCostPanel;
	}

	public Meeting getselectedMeeting() {
		return selectedMeeting;
	}

	public void setselectedMeeting(Meeting selectedMeeting) {
		this.selectedMeeting = selectedMeeting;
	}

	public void viewMeetingCostPanel(Meeting meeting) {
		this.enableCostPanel = true;
		this.selectedMeeting = meeting;

		this.organizer = cmService.organizerPerMeeting(meeting.getId());
	}

	public List<Meeting> findAllMeeting() {
		return meetingCostService.allMeetings();
	}

	public void recordCost() {
		try {
			Meeting meeting = cmService.findMeetingById(meetingId);
			expenditureItem.setMeeting(meeting);
			expenditureItem.setTotalCost(expenditureItem.getQuantity().multiply(expenditureItem.getUnitCost()));
			String msg = meetingCostService.recordMeetingCost(expenditureItem);
			expenditureItem = new ExpenditureItem();
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error", e.getMessage());
		}
	}

	public List<ExpenditureItem> costByMeeting() {
		return meetingCostService.costByMeeting(selectedMeeting.getId());
	}

	public BigDecimal calculateGrandTotal() {
		BigDecimal total = new BigDecimal(0);
		for (ExpenditureItem ei : costByMeeting()) {
			total = total.add(ei.getTotalCost());
		}
		return total;
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
