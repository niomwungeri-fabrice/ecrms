/**
 * 
 */
package rw.mentors.ecrms.usecase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.EvaluationVariable;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.MeetingType;
import rw.mentors.ecrms.service.AdministrationService;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 6, 2017
 */
@Component
@ManagedBean
@ViewScoped
public class Settings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AdministrationService administrationService;

	private String keyWord;
	private String username;

	private List<Employee> filterEmployee;

	private List<Employee> empList;

	private EvaluationVariable evaluationVariable = new EvaluationVariable();
	private Employee employee = new Employee();

	private MeetingType mType = new MeetingType();



	private Institution institution = new Institution();

	private Date today = new Date();

	public void createVariable() {
		try {
			String msg = administrationService.createVariable(evaluationVariable);
			evaluationVariable = new EvaluationVariable();
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void updateVariable(EvaluationVariable evaluationVariable) {
		try {
			String msg = administrationService.updatedVariable(evaluationVariable);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public void deleteVariable(EvaluationVariable evaluationVariable) {
		try {
			String msg = administrationService.deleteVariable(evaluationVariable);
			successMessages("Success", msg);
		} catch (Exception e) {
			errorMessages("Error:", e.getMessage());
		}
	}

	public List<EvaluationVariable> evaluationVariables() {
		return administrationService.allEvaluationVariables();
	}

	public void getVariableObj(EvaluationVariable evaluationVariable) {
		this.evaluationVariable = evaluationVariable;
	}

	public void getEmpObj(Employee e) {
		this.employee = e;
	}

	public void getMtObj(MeetingType meetingType) {
		this.mType = meetingType;
	}

	public void getInstObj(Institution institution) {
		this.institution = institution;
	}

	// formated Date
	public String convertToTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d KK:mm a");
		return outputFormat.format(date);
	}

	// format dates
	public String customeDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		DateFormat outputFormat = new SimpleDateFormat("MMM d");
		return outputFormat.format(date);
	}



	public void updateInsta(Institution institution) {
		try {
			String msg = administrationService.updateInsta(institution);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	public void updateMeetingtype(MeetingType meetingType) {
		try {
			String msg = administrationService.editMeetingtype(meetingType);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}



	public void deleteInsta(Institution institution) {
		try {
			String msg = administrationService.deleteInsta(institution);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	public void deleteEmployee(Employee emp) {
		try {
			String msg = administrationService.deleteEmployee(emp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	public void editEmployee(Employee emp) {
		try {
			String msg = administrationService.updateInfo(emp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	public void activateEmployee(Employee emp) {
		try {
			String msg = administrationService.activateEmployee(emp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	@PostConstruct
	public void init() {
		this.empList = administrationService.findAll();
	}

	public void employeeSearchListener() {
		List<Employee> list = administrationService.findAll();
		for (Employee e : list) {
			if (e.getEmail().contains(keyWord) || e.getFirstName().contains(keyWord)
					|| e.getLastName().contains(keyWord)) {
				this.empList.add(e);
			} else {
				this.empList = new ArrayList<Employee>();
			}
		}
	}

	public EvaluationVariable getEvaluationVariable() {
		return evaluationVariable;
	}

	public void setEvaluationVariable(EvaluationVariable evaluationVariable) {
		this.evaluationVariable = evaluationVariable;
	}

	public List<Institution> instaList() {
		return administrationService.instiList();
	}

	

	public List<Employee> employeeList() {
		return administrationService.findAll();
	}

	public void createInstituion() {
		try {
			String msg = administrationService.createInstitution(institution);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
			institution = new Institution();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:" + e.getMessage(), "Error:" + e.getMessage()));
		}
	}

	
	public void create() {
		try {
			String msg = administrationService.createEmployee(employee);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "Employee Saved successfully"));
			employee = new Employee();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:" + e.getMessage(), "Error:" + e.getMessage()));
		}
	}

	public void activateMeetingType(MeetingType mtType) {
		try {
			String msg = administrationService.activateMeetingType(mtType);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	public void createMType() {
		try {
			String rs = administrationService.createMeetingType(mType);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, rs, null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	// deleting meeting
	public void deleteMType(MeetingType mt) {
		try {
			String res = administrationService.deleteMeetingType(mt);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res, null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error>>" + e.getMessage(), null));
		}
	}

	// all meeting types
	public List<MeetingType> findAllmtypes() {
		return administrationService.findAllType();
	}

	// find all employees
	public List<Employee> getEmployees() {
		return administrationService.findAll();
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public String getUsername() {
		return username;
	}

	public List<Employee> getFilterEmployee() {
		return filterEmployee;
	}

	public void setFilterEmployee(List<Employee> filterEmployee) {
		this.filterEmployee = filterEmployee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public MeetingType getmType() {
		return mType;
	}

	public void setmType(MeetingType mType) {
		this.mType = mType;
	}

	

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<Employee> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Employee> empList) {
		this.empList = empList;
	}

	public void successMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void warningMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void errorMessages(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
