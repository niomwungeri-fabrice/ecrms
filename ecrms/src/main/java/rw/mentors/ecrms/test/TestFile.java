/**
 * 
 */
package rw.mentors.ecrms.test;

import java.io.File;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;

/**
 * @author NIYOMWUNGERI
 * @Date Jul 6, 2017
 */
public class TestFile {

	private static Scanner INPUT;

	public static void main(String... args) {
		try {
			File file = new File("C:/Users/NIYOMWUNGERI/Desktop/dashboardcolors.txt");
			if (file.exists()) {
				INPUT = new Scanner(file);
				System.out.println("Size:" + file.getTotalSpace());
				System.out.println("Size:" + file.getTotalSpace());
				System.out.println("Name:" + file.getName());
				System.out.println("File Type:" + FilenameUtils.getExtension(file.toString()));
				System.out.println("File Content:");
				while (INPUT.hasNext()) {

					System.out.println(INPUT.nextLine());
				}
			} else {
				System.out.println("file not Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
