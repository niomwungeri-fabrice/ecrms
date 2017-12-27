/**
 * 
 */
package rw.mentors.ecrms.test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author NIYOMWUNGERI
 * @Date Sep 22, 2017
 */

public class TestDate {
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
		String today = dateFormat.format(new Date());
		String deadLine = "14 11 2017";
		try {
			Date tDate = dateFormat.parse(today);
			Date deadLin = dateFormat.parse(deadLine);
			long deff = deadLin.getTime() - tDate.getTime();
			System.out.println("Days:"+TimeUnit.DAYS.convert(deff, TimeUnit.MILLISECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
