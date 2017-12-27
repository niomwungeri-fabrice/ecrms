package rw.mentors.ecrms.service;

import java.util.List;

import rw.mentors.ecrms.domain.Employee;
import rw.mentors.ecrms.domain.EvaluationVariable;
import rw.mentors.ecrms.domain.ExecutionEvaluation;
import rw.mentors.ecrms.domain.Institution;
import rw.mentors.ecrms.domain.MeetingType;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
public interface AdministrationService {

	// employee - start
	String createEmployee(Employee emp);

	List<Employee> findAll();

	String updateInfo(Employee emp);

	Employee findEmployeeByEmail(String email);

	String deleteEmployee(Employee employee);

	String activateEmployee(Employee employee);

	// Meeting type -start
	String createMeetingType(MeetingType mType);

	List<MeetingType> findAllType();

	String deleteMeetingType(MeetingType mtype);

	String editMeetingtype(MeetingType mtype);

	MeetingType findByMtType(String mtId);

	String activateMeetingType(MeetingType mtType);

	
	// institution start
	String createInstitution(Institution inst);
	
	Institution createExInstituion(Institution institution);

	List<Institution> instiList();

	String updateInsta(Institution institution);

	String deleteInsta(Institution institution);

	// evaluation variable

	String createVariable(EvaluationVariable evaluationVariable);

	String updatedVariable(EvaluationVariable evaluationVariable);

	String deleteVariable(EvaluationVariable evaluationVariable);

	List<EvaluationVariable> allEvaluationVariables();

	// execution evaluation
	String createEvaluation(ExecutionEvaluation executionEvaluation);

	String updatedEvaluation(ExecutionEvaluation executionEvaluation);

	List<ExecutionEvaluation> allEvaluations();

}
