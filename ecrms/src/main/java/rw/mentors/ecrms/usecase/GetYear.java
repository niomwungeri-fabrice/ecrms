/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;

/**
 * @author NIYOMWUNGERI
 * @Date May 31, 2017
 */
@ManagedBean
public class GetYear implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public int calculateCurrentYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.get(Calendar.YEAR);
	}
}
