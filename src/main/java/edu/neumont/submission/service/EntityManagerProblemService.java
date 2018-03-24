package edu.neumont.submission.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Test;

@Service
public class EntityManagerProblemService implements ProblemService {
	@PersistenceContext EntityManager em;
	
	@Override
	public List<Problem> findAll() {
		return em.createNamedQuery("findAllProblems", Problem.class).getResultList();
	}
	
	@Override
	@Secured("ROLE_ADMIN")
	public Problem getProblem(Long id) {
		return em.find(Problem.class, id);
	}

	@Override
	public Problem getProblemForRound(Long id) {
		TypedQuery<Problem> query = em.createNamedQuery("loadProblemForRound", Problem.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	@Override
	@Transactional
	@Secured("ROLE_ADMIN")
	public Problem addProblem(Problem problem) {
		em.persist(problem);
		return problem;
	}

	@Override
	@Transactional
	@Secured("ROLE_ADMIN")
	public Problem updateProblem(Problem problem) {
		problem = em.merge(problem);
		em.persist(problem);
		return problem;
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void deleteProblem(Long id) {
		em.remove(em.find(Problem.class, id));
	}

	@Override
	@Transactional
	@Secured("ROLE_ADMIN")
	public Test addTestToProblem(Long id, Test test) {
		Problem p = getProblem(id);
		p.addTest(test);
		updateProblem(p);
		Test withId = p.getTests().stream().filter(t -> t.equals(test)).findFirst().get();
		return withId;
	}

/*	@Override
	@Transactional
	@Secured("ROLE_ADMIN")
	public Test updateTestForProblem(Long id, Test test) {
		Problem p = getProblem(id);
		p.addTest(test);
		updateProblem(p);
		Test withId = p.getTests().stream().filter(t -> t.equals(test)).findFirst().get();
		return withId;
	}*/
}
