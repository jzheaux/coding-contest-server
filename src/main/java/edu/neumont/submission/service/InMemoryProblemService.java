package edu.neumont.submission.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Test;

public class InMemoryProblemService implements ProblemService {
	private Map<Long, Problem> problems = new HashMap<Long, Problem>();
	{
		Problem p = new Problem("Step-wise Summation", "Step-wise Summation", "Given a starting number m, an ending number n, and a step value p, return the sum of every pth number from m to n inclusive.");
		problems.put(p.getId(), p);
		Test firstTest = new Test(p, "simple", "1,10,1", "55", 10000, true);
		Test secondTest = new Test(p, "step", "5,25,4", "90", 10000, true);
		Test thirdTest = new Test(p, "negative step", "10,1,-1", "55", 10000, true);
		p.addTest(firstTest);
		p.addTest(secondTest);
		p.addTest(thirdTest);
	}
	
	@Override
	public List<Problem> findAll() {
		return new ArrayList<>(problems.values());
	}
	
	@Override
	public Problem getProblem(Long id) {
		return problems.get(id);
	}
	
	@Override
	public Problem getProblemForRound(Long id) {
		return problems.get(id);
	}

	@Override
	public Problem addProblem(Problem problem) {
		return updateProblem(problem);
	}

	@Override
	public Problem updateProblem(Problem problem) {
		problems.put(problem.getId(), problem);
		return problem;
	}

	@Override
	public void deleteProblem(Long id) {
		problems.remove(id);
	}

	@Override
	public Test addTestToProblem(Long id, Test test) {
		Problem p = getProblem(id);
		p.addTest(test);
		return test;
	}

}
