/**
 * 
 */
package rw.mentors.ecrms.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 6, 2017
 */
@Transactional(propagation = Propagation.REQUIRED)
public abstract class TransactionAware {

	
}
