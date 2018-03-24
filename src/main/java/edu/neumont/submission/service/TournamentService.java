package edu.neumont.submission.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import edu.neumont.submission.auth.Authorized;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Tournament;

@Service
public class TournamentService {
	@PersistenceContext EntityManager em;
	
/*	private Map<Long, Tournament> tournaments = new HashMap<Long, Tournament>();
	{
		Tournament t = new Tournament();
		t.setName("March Madness 2015");
		
		Set<Round> rounds = new HashSet<Round>();
		t.setRounds(rounds);
		
		Round r = new Round();
		r.setId(1L);
		r.setMaxTime(30L);
		r.setName("Round One");
		r.setOpen(true);
		rounds.add(r);
		
		Set<Problem> problems = new HashSet<Problem>();
		r.setProblems(problems);
		
		Problem p = new InMemoryProblemService().getProblem(1L);
		problems.add(p);
		tournaments.put(t.getId(), t);
	}*/
	
	public Tournament getTournament(Long id) {		
		return em.find(Tournament.class, id);
	}
	
	public List<Tournament> getActiveTournaments() {
		TypedQuery<Tournament> query = em.createNamedQuery("findAllActive", Tournament.class);
		return query.getResultList().stream().filter(Authorized.tournamentAccessPredicate).collect(Collectors.toList());
	}
	
	
	@Transactional
	@Secured("ROLE_ADMIN")
	public void addTournament(Tournament t) {
		em.persist(t);
	}
	
	@Transactional
	@Secured("ROLE_ADMIN")
	public void updateTournament(Tournament t) {
		Tournament merged = em.merge(t);
		em.persist(merged);
	}

	@Transactional
	@Secured("ROLE_ADMIN")
	public void updateTournamentWithCoders(Tournament t, List<Integer> coderIds) {
		Query q = em.createNativeQuery("DELETE FROM tournament_coder WHERE tournament_id = ?");
		q.setParameter(1, t.getId());
		q.executeUpdate();

		q = em.createNativeQuery("INSERT INTO tournament_coder (tournament_id, coder_id) VALUES (?, ?)");
		for ( Integer coderId : coderIds ) {
			q.setParameter(1, t.getId());
			q.setParameter(2, coderId);
			q.executeUpdate();
		}
	}

	@Transactional
	@Secured("ROLE_ADMIN")
	public void updateTournamentRoundWithProblems(Round round,
			List<Integer> problemIds) {
		Query q = em.createNativeQuery("DELETE FROM round_problem WHERE round_id = ?");
		q.setParameter(1, round.getId());
		q.executeUpdate();

		q = em.createNativeQuery("INSERT INTO round_problem (round_id, problem_id) VALUES (?, ?)");
		for ( Integer problemId : problemIds ) {
			q.setParameter(1, round.getId());
			q.setParameter(2, problemId);
			q.executeUpdate();
		}
	}

	@Transactional
	public void removeProblemFromRound(Long roundId, Long problemId) {
		Problem p = em.createNamedQuery("loadProblemForRound", Problem.class).setParameter("id", problemId).getSingleResult();
		Round round = p.getRound(roundId);
		round.removeProblem(p);
		em.persist(round);
	}
}