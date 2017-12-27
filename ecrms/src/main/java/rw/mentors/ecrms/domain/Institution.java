/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import rw.mentors.ecrms.dao.InstitutionDao;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
@Entity
@Table(name = "INSTITUTION")
@NamedQueries({ @NamedQuery(name = InstitutionDao.QUERY_NAME.findByEmail, query = InstitutionDao.QUERY.findByEmail) })
public class Institution extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAMES")
	private String name;
	@Column(name = "INSTITUTION_NAME")
	private String institutionName;
	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;
	@Column(name = "PHONE", unique = true, nullable = false)
	private String phoneNumber;
	@Column(name = "POSITION")
	private String position;
	

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "institution")
	private List<Invitation> invitations;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Invitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(List<Invitation> invitations) {
		this.invitations = invitations;
	}

	
	

}
