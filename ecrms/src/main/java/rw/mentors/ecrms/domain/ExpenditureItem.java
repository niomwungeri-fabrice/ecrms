/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import rw.mentors.ecrms.dao.ExpenditureItemDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity

@Table(name = "EXPENDITURE_ITEM")
@NamedQueries({
		@NamedQuery(name = ExpenditureItemDao.QUERY_NAME.costByMeeting, query = ExpenditureItemDao.QUERY.costByMeeting) })
public class ExpenditureItem extends GenericDomain {

	private static final long serialVersionUID = 1L;
	@Column(name = "ITEM_NAME")
	private String itemName;
	@Column(name = "QUANTITY")
	private BigDecimal quantity;
	@Column(name = "UNIT_COST")
	private BigDecimal unitCost;
	@Column(name = "TOTAL_COST")
	private BigDecimal totalCost;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MEETING_UUID")
	private Meeting meeting;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

}
