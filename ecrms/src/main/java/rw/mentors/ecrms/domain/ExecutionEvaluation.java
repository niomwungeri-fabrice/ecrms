package rw.mentors.ecrms.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "EXECUTION_EVALUATION", uniqueConstraints = @UniqueConstraint(columnNames = { "EXECUTION_UUID",
		"EVALUATION_VARIABLE_UUID" }))
public class ExecutionEvaluation extends GenericDomain {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "EXECUTION_UUID")
	private Execution execution;
	@ManyToOne
	@JoinColumn(name = "EVALUATION_VARIABLE_UUID")
	private EvaluationVariable evaluationVariable;

	@Column(name = "POINTS")
	private int points;

	public Execution getExecution() {
		return execution;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public EvaluationVariable getEvaluationVariable() {
		return evaluationVariable;
	}

	public void setEvaluationVariable(EvaluationVariable evaluationVariable) {
		this.evaluationVariable = evaluationVariable;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
