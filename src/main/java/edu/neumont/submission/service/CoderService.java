package edu.neumont.submission.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Submission;

public interface CoderService {

	List<Coder> findAll();
	
	Coder getCoder(Long id);

	Coder addCoder(Coder coder);

	Coder updateCoder(Coder coder);

	void deleteCoder(Long id);

	Coder getByUsername(String username);
	
	Map<Coder, Set<Submission>> getCodersWithSuccessfulSubmissionsForRound(Long roundId);
}