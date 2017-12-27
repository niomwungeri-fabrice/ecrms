/**
 * 
 */
package rw.mentors.ecrms.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import rw.mentors.ecrms.domain.Meeting;
import rw.mentors.ecrms.service.CreatingMeetingService;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 30, 2017
 */
public class MainThingEmail {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:rw/mentors/ecrms/config/app-context.xml");
		CreatingMeetingService statisticService = context.getBean(CreatingMeetingService.class);
		Meeting m = statisticService.findMeetingById("4028b8815d8f1393015d8f196c210004");
		// Recipient's email ID needs to be mentioned.
		String to = "niomwungeri.fabrice@gmail.com";// change accordingly

		// Sender's email ID needs to be mentioned
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

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("INVITATION - OPM");

			// Now set the actual message
			String emailMessage = "<h1 style=font-weight: bold; color: maroon;><center>INVITATION</center></h1></center><br />"
					+ "Office of the Prime minister would like to invite you in the meeting that will take place "
					+ m.getStartTime() + " at " + m.getLocation() + "<br /><br />Meeting Purpose :" + m.getPurpose()
					+ "<br /><br />Time:" + m.getStartTime() + " - " + m.getEndTime()

					+ "<br /><br />Your presence would be appreciated "
					+ "<br /><br /> Best Regards<br />Office of the Prime Minister ";
			message.setContent(emailMessage, "text/html");
			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
