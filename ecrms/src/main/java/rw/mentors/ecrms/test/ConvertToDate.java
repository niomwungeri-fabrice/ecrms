package rw.mentors.ecrms.test;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

public class ConvertToDate {
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File("E:/IFATE.csv"));
			while(in.hasNext()) {
				String caseNo = in.next();
				Date reportingDate = new Date(in.nextLong());
				//String reportingDate = in.next();
				System.out.println("UPDATE CASES SET REPORTED_DATE"+caseNo+"\t"+reportingDate);
			}
			in.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
