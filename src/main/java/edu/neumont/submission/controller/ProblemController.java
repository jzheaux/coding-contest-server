package edu.neumont.submission.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.neumont.submission.auth.Authorized;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;
import edu.neumont.submission.model.Test;
import edu.neumont.submission.service.ProblemService;
import edu.neumont.submission.service.SubmissionService;

@Controller
public class ProblemController {
	@Inject ProblemService problemService;
	@Inject SubmissionService submissionService;
	
	@RequestMapping("/tournament/{id}/round/{roundId}/problem/{problemId}")
	public ModelAndView beginShowProblemWorkflow(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId, @PathVariable("problemId") Long problemId) {
		Problem p = problemService.getProblemForRound(problemId);
		Round r = p.getRound(roundId);
		return Authorized.withRound(r, () -> {
			Submission s = submissionService.getSubmission(r, p);
			return new ModelAndView("/problem/view", "model", s);
		}, () ->  new ModelAndView("/round/countdown", "model", r));
	}

	@RequestMapping(value="/problem/{problemId}/edit")
	@Secured("ROLE_ADMIN")
	public ModelAndView beginProblemEditingWorkflow(@PathVariable("problemId") Long problemId) {
		Problem p = problemService.getProblem(problemId);
		return new ModelAndView("/problem/edit", "model", p);
	}
	
	@RequestMapping(value="/problem/{problemId}/edit", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public ModelAndView commitProblemEditingWorkflow(@PathVariable("problemId") Long problemId, @ModelAttribute("model") Problem problem) {
		Problem old = problemService.getProblem(problemId);
		old.setDescription(problem.getDescription());
		old.setName(problem.getName());
		old.setTitle(problem.getTitle());
		old.setScore(problem.getScore());
		problemService.updateProblem(old);
		return new ModelAndView("/problem/edit", "model", problem);
	}
	
	@RequestMapping(value="/problems")
	@Secured("ROLE_ADMIN")
	public @ResponseBody
	List<Problem> listProblems() {
		return problemService.findAll();
	}
	
	/*@RequestMapping(value="/problem/new")
	@Secured("ADMIN")
	public String beginCreateProblemWorkflow() {
		return "/problem/edit";
	}
	*/
	
	@RequestMapping(value="/problem", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public @ResponseBody
	Problem commitCreateProblemWorkflow(Problem p) {
		problemService.addProblem(p);
		return p;
	}
	
	/*@RequestMapping("/problem/{id}/edit")
	@Secured("ADMIN")
	public ModelAndView beginEditProblemWorkflow(@PathVariable("id") Long id) {
		Problem p = problemService.getProblem(id);
		return new ModelAndView("/problem/edit", "model", p);
	}*/

	@RequestMapping(value="/problem/{id}", method=RequestMethod.PUT)
	@Secured("ROLE_ADMIN")
	public @ResponseBody
	Problem commitEditProblemWorkflow(@PathVariable("id") Long id, Problem problem) {
		Problem p = problemService.getProblem(id);
		p.setTitle(problem.getTitle());
		p.setDescription(problem.getDescription());
		
		problemService.updateProblem(p);
		
		return p;
	}
	
	@RequestMapping(value="/problem/{id}/test/{testId}", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public @ResponseBody 
	Test updateTest(@PathVariable("id") Long id, @PathVariable("testId") Long testId, Test test) {
		Problem p = problemService.getProblem(id);
		Test t = p.getTest(testId);
		t.setInput(test.getInput());
		t.setExpected(test.getExpected());
		t.setMaxTime(test.getMaxTime());
		t.setPublic(test.isPublic());
		problemService.updateProblem(p);
		t.setProblem(null);
		return t;
	}
	
	@RequestMapping(value="/problem/{id}/test", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public @ResponseBody
	Test addTest(@PathVariable("id") Long id, Test test) {
		test = problemService.addTestToProblem(id, test);
		test.setProblem(null); // for JSON recursion
		return test;
	}
	
	@RequestMapping(value="/problem/{id}/test/{testId}", method=RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public @ResponseBody
	void removeTest(@PathVariable("id") Long id, @PathVariable("testId") Long testId) {
		Problem p = problemService.getProblem(id);
		p.removeTest(testId);
		problemService.updateProblem(p);
	}
}
