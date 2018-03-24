package edu.neumont.submission.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.neumont.submission.auth.Authorized;
import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;

@Service
public class EntityManagerCoderService implements CoderService {
	@PersistenceContext EntityManager em;
	@Inject PasswordEncoder pe;
	
	@Override
	public List<Coder> findAll() {
		TypedQuery<Coder> coders = em.createNamedQuery("findAll", Coder.class);
		return coders.getResultList();
	}
	
	@Override
	public Coder getCoder(Long id) {
		return em.find(Coder.class, id);
	}

	@Override
	@Transactional
	public Coder addCoder(Coder coder) {
		coder.setPassword(pe.encode(new String(coder.getPassword())).toCharArray());
		em.persist(coder);
		return coder;
	}

	@Override
	public Coder updateCoder(Coder coder) {
		em.persist(coder);
		return coder;
	}

	@Override
	public void deleteCoder(Long id) {
		em.remove(em.find(Coder.class, id));
	}

	@Override
	public Coder getByUsername(String username) {
		TypedQuery<Coder> q = em.createNamedQuery("byUsername", Coder.class);
		q.setParameter("username", username);
		return q.getSingleResult();
	}

	@Override
	public Map<Coder, Set<Submission>> getCodersWithSuccessfulSubmissionsForRound(Long roundId) {
		TypedQuery<Round> query = em.createNamedQuery("withSubmissions", Round.class);
		query.setParameter("id", roundId);
		Round r = query.getSingleResult();
		
		Map<Coder, Set<Submission>> successfulSubmissionsByCoder = new TreeMap<Coder, Set<Submission>>();
		
		for ( Submission s : r.getSubmissions() ) {
			Coder c = s.getOwner();
			
			Authorized.withCoder(c,
				() -> {
					Set<Submission> set = successfulSubmissionsByCoder.get(c);
					if ( set == null ) {
						set = new TreeSet<Submission>((Submission left, Submission right) -> right.getDate().compareTo(left.getDate()));
						successfulSubmissionsByCoder.put(c, set);
					}
					if ( s.isPassed() ) {
						set.add(s);
					}
					return null;
				});
		}
		
		return successfulSubmissionsByCoder;
	}
}
