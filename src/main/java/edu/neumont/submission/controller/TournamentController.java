package edu.neumont.submission.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.neumont.submission.auth.Authorized;
import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.ProblemView;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;
import edu.neumont.submission.model.Tournament;
import edu.neumont.submission.service.CoderService;
import edu.neumont.submission.service.CoderUserDetailsService.CoderUser;
import edu.neumont.submission.service.ProblemService;
import edu.neumont.submission.service.TournamentService;

@Controller
public class TournamentController {
	@Inject TournamentService tournamentService;
	@Inject CoderService coderService;
	@Inject ProblemService problemService;
	
	@RequestMapping("/tournament/{id}/round/{roundId}/notice")
	public ModelAndView showRoundNotice(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId) {
		Tournament t = tournamentService.getTournament(id);
		Round r = t.getRound(roundId);
		return Authorized.withRound(r, 
				() -> new ModelAndView("/round/notice", "model", r),
				() -> new ModelAndView("/round/countdown", "model", r));
	}
	
	@RequestMapping("/")
	public ModelAndView showTournament() {
		List<Tournament> tournaments = tournamentService.getActiveTournaments();
		
		if ( tournaments.size() == 1 ) {
			Tournament t = tournaments.get(0);
			Round r = t.getNextRound();
			return new ModelAndView("redirect:/tournament/" + t.getId() + "/round/" + r.getId() + "/notice");
		} else {
			return new ModelAndView("/tournament/view", "model", tournaments);
		}
	}
	
	@RequestMapping("/tournament/{id}/round/{roundId}")
	public ModelAndView showRound(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId) {
		CoderUser current = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Tournament t = tournamentService.getTournament(id);
		Round r = t.getRound(roundId);

		return Authorized.withRound(r,
				() -> {
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("id", r.getId());
					model.put("name", r.getName());
					model.put("maxTime", r.getMaxTime());
					model.put("secondsUntilEndTime", r.getSecondsUntilEndTime());
					
					int score = 0;
					Set<ProblemView> pvs = new TreeSet<ProblemView>();
					for ( Problem p : r.getProblems() ) {
						ProblemView pv = new ProblemView(p, current.getCoder(), r);
						if ( pv.isPassed() ) {
							score += p.getScore();
						}
						pvs.add(pv);
					}
					model.put("score", score);
					model.put("problems", pvs);
		
					return new ModelAndView("/round/view", "model", model);
				},
				() ->  new ModelAndView("/round/countdown", "model", r)
		);
	}
	
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/report", method=RequestMethod.GET)
	public ModelAndView showSolutionsReport(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId) {
		Tournament t = tournamentService.getTournament(id);
		Map<Coder, Set<Submission>> submissionsByCoder = coderService.getCodersWithSuccessfulSubmissionsForRound(roundId);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("tournament", t);
		model.put("round", t.getRound(roundId));
		model.put("coders", submissionsByCoder);
		return new ModelAndView("/round/report", "model", model);
	}
	
	@RequestMapping(value="/tournament/new")
	@Secured("ROLE_ADMIN")
	public ModelAndView beginTournamentCreationWorkflow(@ModelAttribute("model") Tournament tournament) {
		return new ModelAndView("/tournament/edit", "availableCoders", coderService.findAll());
	}
	
	@RequestMapping(value="/tournament/new", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String commitTournamentCreationWorkflow(@RequestParam(value="numberOfRounds") Integer numberOfRounds, @ModelAttribute("model") @Valid Tournament tournament) {
		for ( int i = 0; i < numberOfRounds; i++ ) {
			Round round = new Round();
			round.setStartDate(LocalDateTime.now());
			round.setEndDate(round.getStartDate().plus(7, ChronoUnit.DAYS));
			round.setName("Round #" + i);
			round.setMaxTime(86400L * 7);
			tournament.addRound(round);
		}
		tournament.setActive(true);
		tournamentService.addTournament(tournament);
		
		return "redirect:/tournament/" + tournament.getId() + "/edit";
	}
	
	@RequestMapping(value="/tournament/{id}/edit")
	@Secured("ROLE_ADMIN")
	public ModelAndView beginTournamentEditWorkflow(@PathVariable("id") Long id) {
		Tournament t = tournamentService.getTournament(id);
		
		return new ModelAndView("/tournament/edit", "model", t);
	}
	
	@RequestMapping(value="/tournament/{id}/edit", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String commitTournamentEditWorkflow(@PathVariable("id") Long id, HttpServletRequest request) {
		Tournament t = tournamentService.getTournament(id);
		t.setName(request.getParameter("name"));
		t.setPublic("on".equals(request.getParameter("public")));
		t.setActive("on".equals(request.getParameter("active")));
		List<Integer> coderIds = Arrays.asList(request.getParameterValues("coders"))
				.stream().map((e) -> Integer.parseInt(e)).collect(Collectors.toList());
		
		tournamentService.updateTournament(t);
		tournamentService.updateTournamentWithCoders(t, coderIds);
		
		return "redirect:/tournament/" + id + "/edit";
	}
	
	@RequestMapping(value="/tournament/{id}/round/new", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public ModelAndView commitRoundCreationWorkflow(@PathVariable("id") Long id){
		Tournament t = tournamentService.getTournament(id);
		int size = t.getRounds().size();
		Round round = new Round();
		round.setStartDate(LocalDateTime.now());
		round.setEndDate(round.getStartDate().plus(1, ChronoUnit.DAYS));
		round.setName("Round #" + size);
		round.setMaxTime(86400L);
		t.addRound(round);
		tournamentService.updateTournament(t);
		
		return new ModelAndView("redirect:/tournament/" + id + "/edit");
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/edit")
	@Secured("ROLE_ADMIN")
	public ModelAndView beginRoundEditingWorkflow(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId){
		Tournament t = tournamentService.getTournament(id);
		Round r = t.getRound(roundId);
		
		return new ModelAndView("/round/edit", "model", r);
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/edit", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String commitRoundEditingWorkflow(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId, @ModelAttribute("model") Round edited){
		Tournament t = tournamentService.getTournament(id);
		Round round = t.getRound(roundId);
		round.setEndDate(edited.getEndDate());
		round.setMaxTime(edited.getMaxTime());
		round.setName(edited.getName());
		round.setStartDate(edited.getStartDate());
		
		tournamentService.updateTournament(t);
		
		return "redirect:/tournament/" + id + "/round/" + roundId + "/edit";
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/delete", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public ModelAndView commitRoundDeletionWorkflow(@PathVariable("id") Long id, @PathVariable("roundId") Long roundId){
		Tournament t = tournamentService.getTournament(id);
		Round r = t.getRound(roundId);
		t.removeRound(r);
		tournamentService.updateTournament(t);
		
		return new ModelAndView("/tournament/edit", "model", t);
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/problem/new")
	@Secured("ROLE_ADMIN")
	public String beginProblemCreationWorkflow(@PathVariable("id") Long tournamentId, @PathVariable("roundId") Long roundId, @ModelAttribute("model") Problem problem) {
		return "/problem/edit";
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/problem/new", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String commitProblemCreationWorkflow(@PathVariable("id") Long tournamentId, @PathVariable("roundId") Long roundId, @ModelAttribute("model") Problem problem) {
		Tournament t = tournamentService.getTournament(tournamentId);
		Round r = t.getRound(roundId);
		r.addProblem(problem);
		problemService.addProblem(problem);
		tournamentService.updateTournament(t);
		return "redirect:/tournament/{id}/round/{roundId}/edit";
	}
	
	@RequestMapping(value="/tournament/{id}/round/{roundId}/problem/{problemId}/delete", method=RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public String commitProblemDeletionWorkflow(@PathVariable("id") Long tournamentId, @PathVariable("roundId") Long roundId, @PathVariable("problemId") Long problemId) {
		tournamentService.removeProblemFromRound(roundId, problemId);
		
		return "redirect:/tournament/{id}/round/{roundId}/edit";
	}
}
