/**
 * 
 */
package rw.mentors.ecrms.test;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.service.CreatingMeetingService;

class SimpleMail2 {

	public static void sendEmail(String messageEmail, String toAddress) {
		try {
			final String fromEmail = "nyf2k16@gmail.com";
			final String password = "CIERRY12";
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
			props.put("mail.smtp.port", "587"); // TLS Port
			props.put("mail.smtp.auth", "true"); // enable authentication
			props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

			Authenticator auth = new Authenticator() {
				// override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};
			Session session = Session.getInstance(props, auth);

			MimeMessage message = new MimeMessage(session);
			message.setSubject("HTML  mail with images");
			message.setFrom(new InternetAddress(fromEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

			//
			// This HTML mail have to 2 part, the BODY and the embedded image
			//
			MimeMultipart multipart = new MimeMultipart("related");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(messageEmail, "text/html");

			// add it
			multipart.addBodyPart(messageBodyPart);

			// second part (the image)
			messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource("C:\\Users\\NIYOMWUNGERI\\Desktop\\logo-OPM-small.png");
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<image>");

			// add it
			multipart.addBodyPart(messageBodyPart);

			// put everything together
			message.setContent(multipart);

			Transport.send(message, message.getRecipients(Message.RecipientType.TO));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:rw/mentors/ecrms/config/app-context.xml");
		CreatingMeetingService statisticService = context.getBean(CreatingMeetingService.class);
		Meeting m = statisticService.findMeetingById("4028b8815d8f1393015d8f196c210004");
		String htmlText = "<center><img src=\"cid:image\"></center><br/><center><img src=" + "C:\\Users\\NIYOMWUNGERI\\Desktop\\logo-OPM-small.png>"
				+ "<h1 style=font-weight: bold; color: maroon;>INVITATION</h1></center><br />"
				+ "Office of the Prime minister would like to invite you in the meeting that will take place "
				+ m.getStartTime() + "at" + m.getLocation()
				+ "<br /><br /><br />Meeting Items and presenters is attached on this email.<br /><br />Meeting Purpose :"
				+ m.getPurpose() + "<br />Time:" + m.getStartTime() + "-" + m.getEndTime()
				+ "<br /> PLEASE confirm your attendance by clicking Here <br /><br />"
				+ "Office of the Prime Minister.";
	

		System.out.println("Sending mail...");
		sendEmail(htmlText, "niomwungeri.fabrice@gmail.com");
		System.out.println("Sent Successfully");
	}
}