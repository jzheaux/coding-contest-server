package edu.neumont.submission.model;

import java.time.LocalDateTime;
import java.util.Set;

public class ProblemView implements Comparable<ProblemView> {
	private Problem problem;
	private Coder coder;
	private Round round;
	
	public ProblemView(Problem problem, Coder coder, Round round) {
		this.problem = problem;
		this.coder = coder;
		this.round = round;
	}
	
	public Long getTimeLeftInRound() {
		return round.getSecondsUntilEndTime();
	}
	
	public Long getId() {
		return problem.getId();
	}
	
	public Long getRoundId() {
		return round.getId();
	}
	
	public Long getTournamentId() {
		return round.getTournament().getId();
	}
	
	public String getTitle() {
		return problem.getTitle();
	}
	
	public String getDescription() {
		return problem.getDescription();
	}

	public Integer getScore() {
		return problem.getScore();
	}
	
	public Set<Test> getTests() {
		return problem.getTests();
	}
	
	public String getPreferredLanguage() {
		return coder.getPreferredLanguage().toString();
	}
	
	public Integer getNumberOfSubmissions() {
		Set<Submission> submissions = problem.getSubmissions();
		int count = 0;
		for ( Submission s : submissions ) {
			if ( s.getOwner().equals(coder) ) {
				count++;
			}
		}
		return count;
	}
	
	public LocalDateTime getLastSubmissionTime() {
		Set<Submission> submissions = problem.getSubmissions();
		LocalDateTime latestDate = null;
		for ( Submission s : submissions ) {
			if ( s.getOwner().equals(coder) && ( latestDate == null || latestDate.isAfter(s.getDate())) ) {
				latestDate = s.getDate();
			}
		}
		return latestDate;
	}
	
	public boolean isPassed() {
		Set<Submission> submissions = problem.getSubmissions();
		for ( Submission s : submissions ) {
			if ( s.getOwner().equals(coder) && s.isPassed() ) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(ProblemView o) {
		return this.problem.getTitle().compareTo(o.problem.getTitle());
	}
}
