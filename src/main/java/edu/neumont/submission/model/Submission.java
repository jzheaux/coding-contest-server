package edu.neumont.submission.model;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="SUBMISSION")
@NamedQueries(@NamedQuery(
		name="findByCoderRoundProblem",
		query="FROM Submission s WHERE s.owner.id = :coderId AND s.round.id = :roundId AND s.problem.id = :problemId"
		))
public class Submission {
	@Id
	@Column
	@GeneratedValue(generator="seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq", sequenceName="seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name="CODER_ID")
	private Coder owner;
	
	@Column
	private String location;
	
	@Transient
	private String code;
	
	@Column(name="IS_PASSED")
	private boolean passed;
	
	@Enumerated
	@Column
	private Language language;
	
	@Column
	private LocalDateTime date;
	
	@ManyToOne
	@JoinColumn(name="PROBLEM_ID")
	private Problem problem;

	@ManyToOne
	@JoinColumn(name="ROUND_ID")
	private Round round;

	@Transient
	private Set<SubmissionResult> tests = new HashSet<SubmissionResult>();

	@Transient
	private List<String> messages = new ArrayList<String>();
	
	public Submission() {}
	
	public Submission(Coder owner, String location, String code, 
			Language language, Problem problem, Round round) {
		this.owner = owner;
		this.location = location;
		this.language = language;
		this.problem = problem;
		this.date = LocalDateTime.now();
		this.round = round;
		this.code = code;
		//this.round.addSubmission(this);
	}
	
	public Submission(Long id, Coder owner, String location, String code,
			Language language, Problem problem, Round round) {
		this(owner, location, code, language, problem, round);
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Coder getOwner() {
		return owner;
	}

	public void setOwner(Coder owner) {
		this.owner = owner;
	}
	

	public String getLocation() {
		return location;
	}


	public Language getLanguage() {
		return language;
	}

	public Problem getProblem() {
		return problem;
	}

	public Round getRound() {
		return round;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public String getCode() {
		if ( code == null && location != null ) {
			try {
				byte[] b = Files.readAllBytes(Paths.get(new File(location).getAbsolutePath()));
				code = new String(b);
			} catch (IOException e) {
				// this is a nice-to-have UI help for the user, so we don't want to blow up if it fails
				e.printStackTrace();
			}
		}
		return code;
	}
	
	public Set<SubmissionResult> getResults() {
		return Collections.unmodifiableSet(tests);
	}
	
	
	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public boolean updatePassed() {
		for ( SubmissionResult result : tests ) {
			if ( !result.isPassed() ) {
				return false;
			}
		}
		return true;
	}

	public List<String> getMessages() {
		return Collections.unmodifiableList(messages);
	}
	
	public void addErrors(List<String> errorMessages) {
		messages.addAll(errorMessages);
	}
	
	public void addResult(Test test, String actual, long time) {
		tests.add(new SubmissionResult(this, test, actual, time));
		this.passed = updatePassed();
	}
	
	public void addResult(Test test, List<String> errorMessages) {
		tests.add(new SubmissionResult(this, test, errorMessages));
		this.passed = updatePassed();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public void setRound(Round round) {
		this.round = round;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof Submission ) {
			Submission that = (Submission)obj;
			return that.id == null ? super.equals(obj) : that.id.equals(this.id);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id == null ? super.hashCode() : this.id.hashCode();
	}
}
