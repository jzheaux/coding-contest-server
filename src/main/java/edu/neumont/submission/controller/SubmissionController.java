package edu.neumont.submission.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.neumont.submission.auth.Authorized;
import edu.neumont.submission.model.Language;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;
import edu.neumont.submission.model.SubmissionResult;
import edu.neumont.submission.service.ProblemService;
import edu.neumont.submission.service.SubmissionService;

@Controller
public class SubmissionController {
	@Inject SubmissionService submissionService;
	@Inject ProblemService problemService;
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/problem/{problemId}/solution/test", method=RequestMethod.POST)
	public @ResponseBody
	Submission testSubmission(@PathVariable("roundId") Long roundId, @PathVariable("problemId") Long problemId, @RequestParam("code") String code, @RequestParam("language") String language) {
		Language l = Language.valueOf(language);
		Problem p = problemService.getProblemForRound(problemId);
		Round r = p.getRound(roundId);
		
		return Authorized.withRound(r, () -> {
			Submission s = submissionService.testSubmission(r, p, "Solution." + ( l == Language.JAVA ? "java" : "cs" ), l, code);
			return wrapSubmission(s);
		});
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/problem/{problemId}/solution", method=RequestMethod.POST)
	public @ResponseBody
	Submission submitSubmission(@PathVariable("roundId") Long roundId, @PathVariable("problemId") Long problemId, @RequestParam("code") String code, @RequestParam("language") String language) {
		Language l = Language.valueOf(language);
		Problem p = problemService.getProblemForRound(problemId);
		Round r = p.getRound(roundId);
		
		return Authorized.withRound(r, () -> {
			Submission s = submissionService.postSubmission(r, p, "Solution." + ( l == Language.JAVA ? "java" : "cs" ), l, code);
			return wrapSubmission(s);
		});
	}
	
	@RequestMapping(value="/submission/{id}", method=RequestMethod.POST)
	public @ResponseBody
	Submission resubmitSolution(@PathVariable("id") Long id, @RequestParam("code") String code, @RequestParam("language") String language) {
		// NOTE: No auth here, thinking about the best way to restrict
		Language l = Language.valueOf(language);
		Submission s =  submissionService.putSubmission(id, "Solution." + ( l == Language.JAVA ? "java" : "cs" ), l, code);
		
		return wrapSubmission(s);
	} 
	
	private Submission wrapSubmission(Submission s) {
		return new Submission() {
			@Override
			public Long getId() {
				return s.getId();
			}

			@Override
			public List<String> getMessages() {
				return s.getMessages();
			}
			
			public boolean isPassed() {
				return s.isPassed();
			}
			
			public Map<Long, SubmissionResult> getMap() {
				Map<Long, SubmissionResult> map = new HashMap<Long, SubmissionResult>();
				for ( SubmissionResult sr : s.getResults() ) {
					if ( sr.isPublic() )
						map.put(sr.getTestId(), sr);
				}
				return map;
			}
		};
	}
}
