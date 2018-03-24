package edu.neumont.submission.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.neumont.submission.model.Language;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.ProblemClosedException;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;
import edu.neumont.submission.model.SubmissionExecutionException;
import edu.neumont.submission.model.SubmissionUploadException;
import edu.neumont.submission.model.Test;
import edu.neumont.submission.service.CoderUserDetailsService.CoderUser;

@Service
public class SubmissionServiceImpl implements SubmissionService {
	@PersistenceContext EntityManager em;
	
	@Inject ProblemService problemService;
	
	@Override
	public Submission getSubmission(Long submissionId) {
		return em.find(Submission.class, submissionId);
	}

	@Override
	@Transactional
	public Submission postSubmission(Round r, Problem p, String fileName, Language language, String source) {
		CoderUser owner = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		SubmissionBuilder sb = new SubmissionBuilder(source).owner(owner.getCoder()).location(fileName).language(language);
		
		Submission s = testSubmission(r, p, sb.problem(p).round(r), p.getTests());
		em.persist(s);
		return s;
	}

	@Override
	@Transactional
	public Submission putSubmission(Long submissionId, String fileName, Language language, String source) {
		CoderUser owner = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Submission old = em.find(Submission.class, submissionId);

		if ( old.getOwner().equals(owner.getCoder()) ) {
			SubmissionBuilder sb = new SubmissionBuilder(source).fromExisting(old).location(fileName).language(language);
		
			Submission s = testSubmission(old.getRound(), old.getProblem(), sb.problem(old.getProblem()).round(old.getRound()), old.getProblem().getTests());
			em.merge(s);
			return s;
		}
		
		throw new SubmissionUploadException("Wrong owner");
	}
	
	@Override
	public Submission testSubmission(Round r, Problem p, String fileName, Language language, String source) {
		CoderUser owner = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		SubmissionBuilder sb = new SubmissionBuilder(source).owner(owner.getCoder()).location(fileName).language(language);

		return testSubmission(r, p, sb.problem(p), p.getExpectations());
	}
	
	private Submission testSubmission(Round r, Problem p, SubmissionBuilder sb, Set<Test> tests) {
		if ( r.getStartDate().isBefore(LocalDateTime.now()) && r.getEndDate().isAfter(LocalDateTime.now()) ) {
			Submission s = sb.build();
			
			// run tests
			ExecutorService es = Executors.newSingleThreadExecutor();
			
			Future<Submission> toPersist = es.submit(new SubmissionCallable(s, tests));
			
			// persist submission
			try {
				s = toPersist.get();
			} catch ( ExecutionException | InterruptedException e ) {
				throw new SubmissionExecutionException(String.format("Failed to execute submission %s for problem %s", s.getLocation(), p.getId()), e);
			}

			return s;
		} else {
			throw new ProblemClosedException(String.format("This problem is open from %s to %s", p.getStartDate(), p.getEndDate()));
		}
	}

	@Override
	public void deleteSubmission(Long submissionId) {
		em.remove(em.find(Submission.class, submissionId));
	}

	@Override
	public Submission getSubmission(Round r, Problem p) {
		CoderUser owner = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		TypedQuery<Submission> query = em.createNamedQuery("findByCoderRoundProblem", Submission.class);
		query.setParameter("coderId", owner.getCoder().getId());
		query.setParameter("roundId", r.getId());
		query.setParameter("problemId", p.getId());
		
		List<Submission> submissions = query.getResultList();
		
		if ( submissions == null || submissions.isEmpty() ) {
			return new Submission(owner.getCoder(), null, null, null, p, r);
		} else {
			return submissions.iterator().next();
		}
	}
	
	
}
