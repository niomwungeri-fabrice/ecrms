package rw.mentors.ecrms.test;

public class PhoneNumberValidator {

	public static void main(String[] args) {

		System.out.println("Phone number 078 374 1086 validation result: " + validatePhoneNumber("0723741086"));
	}

	private static boolean validatePhoneNumber(String phoneNo) {
		char[] p = phoneNo.toCharArray();
		if (p[0] != '0') {
			System.out.println("1.Passed");
			return false;
		} else if (p[1] != '7') {
			System.out.println("2.Passed");
			return false;
		} else if (p[2] != '8' && p[2] != '2' && p[2] != '5') {
			System.out.println("3.Passed");
			return false;
		} else {
			System.out.println("success");
			return true;
		}
	}
}
