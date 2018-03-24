package edu.neumont.submission.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Submission;

public class InMemoryCoderService implements CoderService {
	private Map<Long, Coder> coders = new HashMap<Long, Coder>();
	
	@Override
	public List<Coder> findAll() {
		return new ArrayList<Coder>(coders.values());
	}
	
	@Override
	public Coder getCoder(Long id) {
		return coders.get(id);
	}

	@Override
	public Coder addCoder(Coder coder) {
		return updateCoder(coder);
	}

	@Override
	public Coder updateCoder(Coder coder) {
		coders.put(coder.getId(), coder);
		return coder;
	}

	@Override
	public void deleteCoder(Long id) {
		coders.remove(id);
	}

	@Override
	public Coder getByUsername(String username) {
		for ( Map.Entry<Long, Coder> coder : coders.entrySet() ) {
			if ( coder.getValue().getUsername().equals(username) ) {
				return coder.getValue();
			}
		}
		return null;
	}

	@Override
	public Map<Coder, Set<Submission>> getCodersWithSuccessfulSubmissionsForRound(
			Long roundId) {
		return new HashMap<Coder, Set<Submission>>();
	}
}
