package rw.mentors.ecrms.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.mentors.ecrms.dao.EmployeeDao;
import rw.mentors.ecrms.dao.EvaluationVariableDao;
import rw.mentors.ecrms.dao.ExecutionEvaluationDao;
import rw.mentors.ecrms.dao.InstitutionDao;
import rw.mentors.ecrms.dao.MeetingTypeDao;
import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.EvaluationVariable;
import rw.mentors.ecrms.domain.ExecutionEvaluation;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.MeetingType;
import rw.mentors.ecrms.exception.DataManipulationException;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 6, 2017
 */
@Service
public class AdministrationServiceImpl extends TransactionAware implements AdministrationService {

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private MeetingTypeDao mTypDao;

	@Autowired
	private InstitutionDao instDao;

	@Autowired
	private EvaluationVariableDao evaluationVariableDao;
	@Autowired
	private ExecutionEvaluationDao executionEvaluationDao;

	@Override
	public String createEmployee(Employee emp) {
		try {
			Employee employee = employeeDao.save(emp);
			return employee.getFirstName() + " Created Successfully";
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException("Employee '" + emp.getEmail() + "' Already Exist");
		}
	}

	@Override
	public List<Employee> findAll() {
		return employeeDao.findAll();
	}

	@Override
	public String updateInfo(Employee emp) {
		try {
			Employee e = employeeDao.update(emp);
			return e.getFirstName() + " Updated Successfully ";
		} catch (Exception e) {
			throw new DataManipulationException("Error Contact Administrator");
		}

	}

	@Override
	public Employee findEmployeeByEmail(String email) {
		try {
			return employeeDao.findByEmail(email);
		} catch (Exception e) {
			throw new DataManipulationException("Employe Not found");
		}
	}

	@Override
	public String deleteEmployee(Employee employee) {
		try {
			employeeDao.delete(employee);
			return "Employee Deleted Successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Employee not found");
		}
	}

	@Override
	public String createMeetingType(MeetingType mType) {
		try {
			MeetingType mt = mTypDao.save(mType);
			return mt.getName() + " Added Successfully";
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Meeting type not created");
		}
	}

	@Override
	public List<MeetingType> findAllType() {
		return mTypDao.findAll();
	}

	@Override
	public String deleteMeetingType(MeetingType mtype) {
		try {
			mTypDao.delete(mtype);
			return " Deleted Successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Meeting not deleted");
		}
	}

	public String editMeetingtype(MeetingType mtype) {
		try {
			mTypDao.update(mtype);
			return " Meeting Type Updated Successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Meeting Type not updated");
		}
	}

	@Override
	public MeetingType findByMtType(String mtId) {
		return mTypDao.findById(mtId);
	}

	@Override
	public String activateEmployee(Employee emp) {
		try {

			Employee employee = employeeDao.findByEmail(emp.getEmail());
			if (employee.isState()) {
				employee.setState(false);
				employeeDao.update(employee);
				return "Employee Disabled";
			} else {
				employee.setState(true);
				employeeDao.update(employee);
				return "Employee unabled";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Operation failed" + e.getMessage());
		}
	}

	public String activateMeetingType(MeetingType mtType) {
		try {

			MeetingType mType = mTypDao.findById(mtType.getId());
			if (mType.isState()) {
				mType.setState(false);
				mTypDao.update(mType);
				return "Meeting type Disabled";
			} else {
				mType.setState(true);
				mTypDao.update(mType);
				return "Meeting type unabled";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Operation failed" + e.getMessage());
		}
	}

	@Override
	public String createInstitution(Institution inst) {
		try {
			instDao.save(inst);
			return " Created Successfully";
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException("Institution already exist, Search in contact tab!");
		} catch (Exception e) {
			throw new DataManipulationException("External Entity not created, Contact Admin");
		}
	}

	@Override
	public List<Institution> instiList() {

		return instDao.findAll();
	}

	@Override
	public String updateInsta(Institution institution) {
		try {
			instDao.update(institution);
			return " Instution Updated Successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Institution not Updated, Contact Admin");
		}
	}

	@Override
	public String deleteInsta(Institution institution) {
		try {
			instDao.delete(institution);
			return " Institution Deleted Successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Institution not Deleted, Contact Admin");
		}
	}

	@Override
	public String createVariable(EvaluationVariable evaluationVariable) {
		try {
			evaluationVariableDao.save(evaluationVariable);
			return " Indicator added successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Indicator not created, Contact Admin");
		}
	}

	@Override
	public String updatedVariable(EvaluationVariable evaluationVariable) {
		try {
			evaluationVariableDao.update(evaluationVariable);
			return " Indicator updated successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Indicator not updated, Contact Admin");
		}
	}

	@Override
	public String deleteVariable(EvaluationVariable evaluationVariable) {
		try {
			evaluationVariableDao.update(evaluationVariable);
			return " Indicator deleted successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Indicator not deleted, Contact Admin");
		}
	}

	@Override
	public List<EvaluationVariable> allEvaluationVariables() {
		return evaluationVariableDao.findAll();
	}

	@Override
	public String createEvaluation(ExecutionEvaluation executionEvaluation) {
		try {
			executionEvaluationDao.save(executionEvaluation);
			return " Execution rated successfully";
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException("Execution has been rated already!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataManipulationException("Error, Contact Admin>>" + e.getMessage());
		}
	}

	@Override
	public String updatedEvaluation(ExecutionEvaluation executionEvaluation) {
		try {
			executionEvaluationDao.update(executionEvaluation);
			return " Execution changed successfully";
		} catch (Exception e) {
			throw new DataManipulationException("Error, Contact Admin");
		}
	}

	@Override
	public List<ExecutionEvaluation> allEvaluations() {
		return executionEvaluationDao.findAll();
	}

	@Override
	public Institution createExInstituion(Institution institution) {
		try {
			return instDao.save(institution);
		} catch (ConstraintViolationException e) {
			throw new DataManipulationException(e.getMessage());
		} catch (Exception e) {
			throw new DataManipulationException(e.getMessage());
		}
	}

}
