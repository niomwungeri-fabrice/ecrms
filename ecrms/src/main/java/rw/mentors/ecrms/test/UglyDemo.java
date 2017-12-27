/**
 * 
 */
package rw.mentors.ecrms.test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import rw.mentors.ecrms.domain.EExecutionStatus;
import rw.mentors.ecrms.domain.Invitation;
import rw.mentors.ecrms.service.AttendanceService;

/**
 * Ugly demo of how to use jFreeChart with iText. The output will have the
 * stair-case effect.
 * 
 * @author Jee Vang
 *
 */
public class UglyDemo {

	public static void main(String[] args) {
		convertToPdf(getBarChart(), 840, 480, "C:\\Users\\NIYOMWUNGERI\\Desktop\\fileName.pdf");
		System.out.println("Success");
	}

	@SuppressWarnings("resource")
	public static void convertToPdf(JFreeChart jfc, int w, int h, String fileName) {
		Document doc = new Document(new Rectangle(w, h));
		doc = new Document(PageSize.A4.rotate());
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath:rw/mentors/ecrms/config/app-context.xml");
			AttendanceService attendanceService = context.getBean(AttendanceService.class);

			PdfWriter pdfWriter;
			OutputStream out = new FileOutputStream(fileName);
			pdfWriter = PdfWriter.getInstance(doc, out);
			doc.open();
			Image img = Image.getInstance("C:\\Users\\NIYOMWUNGERI\\Desktop\\itcg-logo.png");
			Paragraph header = new Paragraph();
			header.setAlignment(Element.ALIGN_CENTER);
			header.add(img);
			doc.add(header);

			doc.add(new Paragraph("RWANDA - KIGALI\nKG 1 Roundabout, Kigali\nPO BOX 1334 Kigali"));
			doc.add(new Paragraph("                                          "));
			Paragraph p = new Paragraph("Attendance List ",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			PdfPTable table = new PdfPTable(3);
			doc.add(table);
			table.addCell("Names");
			table.addCell("Attended");
			table.addCell("Confirmed");

			for (Invitation m : attendanceService.findInvited("4028b8815d5fd060015d5fd2b9f60000")) {
				if (m.getEmployee() == null) {
					table.addCell(m.getInstitution().getName());
				} else {
					table.addCell(m.getEmployee().getFirstName() + " " + m.getEmployee().getLastName());
				}
				if (m.isAttended()) {
					table.addCell("Yes");
				} else {
					table.addCell("No");
				}
				if (m.isConfirmed()) {
					table.addCell("Yes");
				} else {
					table.addCell("No");
				}
			}
			doc.add(table);
			doc.add(new Paragraph("              u                            "));
			doc.add(new Paragraph("                                          "));
			PdfContentByte pcb = pdfWriter.getDirectContent();
			PdfTemplate tp = pcb.createTemplate(w, h);
			Graphics2D g2d = tp.createGraphics(w, h, new DefaultFontMapper());
			Rectangle2D r2d = new Rectangle2D.Double(0, 0, w, h);
			jfc.draw(g2d, r2d);
			g2d.dispose();
			pcb.addTemplate(tp, 0, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		doc.close();
	}

	public static JFreeChart getBarChart() {
		DefaultCategoryDataset dcd = new DefaultCategoryDataset();
		for (EExecutionStatus e : EExecutionStatus.values()) {
			if(e.toString().equals(EExecutionStatus.APPROVED.toString())){
				dcd.setValue(4, "Status", e);
			}else if(e.toString().equals(EExecutionStatus.REJECTED.toString())){
				dcd.setValue(6, "Status", e);
			
			}else if(e.toString().equals(EExecutionStatus.IN_PROGROCESS.toString())){
				dcd.setValue(7, "Status", e);
			}else{
				dcd.setValue(2, "Status", e);
			}
			
		}
		return ChartFactory.createBarChart("Empoyee Performance Based On Execution", " Status", "Weighted Averaged",
				dcd, PlotOrientation.VERTICAL, false, true, false);
	}
}
