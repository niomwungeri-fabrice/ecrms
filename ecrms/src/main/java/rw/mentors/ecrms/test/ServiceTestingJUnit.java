/**
 * 
 */
package rw.mentors.ecrms.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import rw.mentors.ecrms.service.CreatingMeetingService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 12, 2017
 */
public class ServiceTestingJUnit extends AbtractTestJUnit {

	@Autowired
	private CreatingMeetingService adminService;

	@Test
	public void test() {
		Assert.notNull(adminService.findAllMeetingType());
	}
}
