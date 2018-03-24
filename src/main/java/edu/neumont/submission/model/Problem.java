package edu.neumont.submission.model;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.io.IOUtils;

@Entity
@Table(name="PROBLEM")
@NamedQueries({
		@NamedQuery(
				name="loadProblemForRound",
				query="FROM Problem p JOIN FETCH p.rounds WHERE p.id = :id"),
		@NamedQuery(
				name="findAllProblems",
				query="FROM Problem p")
})
public class Problem {
	@Id
	@Column
	@GeneratedValue(generator="seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq", sequenceName="seq")
	private Long id;
	
	@Column
	private String title;
	
	@Column(length=2000)
	private String description;
	
	@Column
	private String name;
	
	@Column(name="SCORE", columnDefinition="INT default '1'")
	private Integer score;
	
	@Column(name="START_DATE")
	private LocalDateTime startDate;
	
	@Column(name="END_DATE")
	private LocalDateTime endDate;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="problem")
	private final Set<Test> tests = new HashSet<>();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="problem")
	private final Set<Submission> submissions = new HashSet<>();
	
	@ManyToMany(mappedBy="problems")
	private final Set<Round> rounds = new HashSet<>();
	
	public Problem() {}
	
	public Problem(String name, String title, String description) {
		this.name = name;
		this.title = title;
		this.description = description;
		startDate = LocalDateTime.now();
		endDate = LocalDateTime.now().plusYears(23);
	}
	
	public Problem(String name, String title, InputStream file) throws IOException {
		this.name = name;
		this.title = title;
		byte[] description = IOUtils.toByteArray(file);
		this.description = new String(description);
	}

	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Integer getScore() {
		return score;
	}
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	
	public void addTest(Test test) {
		tests.add(test);
		test.setProblem(this);
	}
	
	public Set<Test> getTests() {
		return Collections.unmodifiableSet(tests);
	}
	
	public Set<Test> getExpectations() {
		Set<Test> expectations = new HashSet<Test>();
		for ( Test test : getTests() ) {
			if ( test.isPublic() ) {
				expectations.add(test);
			}
		}
		
		return expectations;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Test getTest(Long testId) {
		for ( Test test : tests ) {
			if ( testId.equals(test.getId()) ) {
				return test;
			}
		}
		return null;
	}
	
	public void removeTest(Long testId) {
		Test test = getTest(testId);
		tests.remove(test);
	}
	
	public Set<Submission> getSubmissions() {
		return Collections.unmodifiableSet(submissions);
	}
	
	public void addSubmission(Submission s) {
		submissions.add(s);
	}
	
	public void addToRound(Round round) {
		rounds.add(round);
	}
	
	public void removeFromRound(Round round) {
		rounds.remove(round);
	}

	public Round getRound(Long roundId) {
		for ( Round r : rounds ) {
			if ( r.getId().equals(roundId) ) {
				return r;
			}
		}
		return null;
	}
}