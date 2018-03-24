package edu.neumont.submission.service;
import java.util.List;

import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Test;


public interface ProblemService {
	List<Problem> findAll();
	Problem getProblem(Long id);
	Problem getProblemForRound(Long id);
	Problem addProblem(Problem problem);
	Problem updateProblem(Problem problem);
	void deleteProblem(Long id);
	Test addTestToProblem(Long id, Test test);
}
