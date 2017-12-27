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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.service.CreatingMeetingService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 12, 2017
 */
public class ConsoleTesting {

	public static void sendEmail(String emailMessage, String toAddress) {

		try {

			final String fromEmail = "nyf2k16@gmail.com";
			final String password = "CIERRY12";

			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
			props.put("mail.smtp.port", "587"); // TLS Port
			props.put("mail.smtp.auth", "true"); // enable authentication
			props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

			// create Authenticator object to pass in Session.getInstance
			// argument
			Authenticator auth = new Authenticator() {
				// override the getPasswordAuthentication method
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			};
			Session session = Session.getInstance(props, auth);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));

			message.setRecipients(Message.RecipientType.TO, toAddress);

			message.setSubject("Reminder");
			message.setText(emailMessage);
			message.setContent(emailMessage, "text/html");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource("C:\\images\\jht.gif");
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID", "<image>");

			System.out.println("Sending...");
			Transport.send(message);
			System.out.println("Email Sent");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Mail not Sent Error>" + ex.getMessage().getClass());

		}

	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:rw/mentors/ecrms/config/app-context.xml");
		CreatingMeetingService statisticService = context.getBean(CreatingMeetingService.class);
		Meeting m = statisticService.findMeetingById("4028b8815d8f1393015d8f196c210004");
		String msg = "<center><img src=" + "C:\\Users\\NIYOMWUNGERI\\Desktop\\logo-OPM-small.png>"
				+ "<h1 style=font-weight: bold; color: maroon;>INVITATION</h1></center><br />"
				+ "Office of the Prime minister would like to invite you in the meeting that will take place "
				+ m.getStartTime() + " at " + m.getLocation()
				+ "<br /><br /><br />Meeting Items and presenters is attached on this email.<br /><br />Meeting Purpose :"
				+ m.getPurpose() + "<br />Time:" + m.getStartTime() + " - " + m.getEndTime()
				+ "<br /><br />" + "Office of the Prime Minister.";

		sendEmail(msg, "niomwungeri.fabrice@gmail.com");
	}

}
