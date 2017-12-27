package rw.mentors.ecrms.test;

import java.security.MessageDigest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import rw.mentors.ecrms.domain.Execution;
import rw.mentors.ecrms.service.AdministrationService;
import rw.mentors.ecrms.service.MeetingReviewService;
import rw.mentors.ecrms.service.UserService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 6, 2017
 */
public class ServiceTestingMain {

	public static String md5(String password) {
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

	public void searchByDate() throws ParseException {

	}

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) {

		try {

			ApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath:rw/mentors/ecrms/config/app-context.xml");
			UserService service = context.getBean(UserService.class);
			AdministrationService administrationService = context.getBean(AdministrationService.class);
			MeetingReviewService msService = context.getBean(MeetingReviewService.class);

			List<Execution> executions = new ArrayList<Execution>();
			for (Execution execution : msService.allExecutions()) {
				if (execution.getAssignedTo().getEmail().equals("niomwungeri.fabrice@gmail.com")) {
					executions.add(execution);
				}
			}

			System.out.println(executions.size());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
	}
}
