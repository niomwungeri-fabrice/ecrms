package rw.mentors.ecrms.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER")
public class Order extends GenericDomain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private long costInCents;
	private boolean isComplete;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCostInCents() {
		return costInCents;
	}

	public void setCostInCents(long costInCents) {
		this.costInCents = costInCents;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public void updateOrder(Order original, Order updated) {
		// Update the other fields of the order
		original.setComplete(updated.isComplete());
	}

}
