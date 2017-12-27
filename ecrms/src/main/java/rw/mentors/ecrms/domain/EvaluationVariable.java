/**
 * 
 */
package rw.mentors.ecrms.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author NIYOMWUNGERI
 *
 */
@Entity
@Table(name = "EVALUATION_VARIABLE")
public class EvaluationVariable extends GenericDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "NAME")
	private String name;

	@Transient
	private int score;

	@OneToMany(mappedBy = "evaluationVariable")
	private List<ExecutionEvaluation> evaluations;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public List<ExecutionEvaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<ExecutionEvaluation> evaluations) {
		this.evaluations = evaluations;
	}

}
