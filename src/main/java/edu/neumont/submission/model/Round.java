package edu.neumont.submission.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ROUND")
@NamedQueries(@NamedQuery(
	name="withSubmissions",
	query="FROM Round r JOIN FETCH r.submissions WHERE r.id = :id"
		))
public class Round {
	@Id
	@Column
	@SequenceGenerator(name="seq", sequenceName="seq")
	@GeneratedValue(generator="seq", strategy=GenerationType.SEQUENCE)
	private Long id;

	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	@JoinTable(name="ROUND_PROBLEM",
		joinColumns=@JoinColumn(name="ROUND_ID", nullable=false),
		inverseJoinColumns=@JoinColumn(name="PROBLEM_ID", nullable=false))
	private Set<Problem> problems = new HashSet<Problem>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.REMOVE, orphanRemoval=true, mappedBy="round")
	private Set<Submission> submissions = new HashSet<Submission>();
	
	@ManyToOne
	@JoinColumn(name="TOURNAMENT_ID")
	private Tournament tournament;
	
	@Column(name="MAX_TIME")
	private Long maxTime;
	
	@Column
	private String name;
	
	@Column(name="START_DATE")
	private LocalDateTime startDate;
	
	@Column(name="END_DATE")
	private LocalDateTime endDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Set<Problem> getProblems() {
		return problems;
	}
	
	public void addProblem(Problem problem) {
		problems.add(problem);
		problem.addToRound(this);
	}
	
	public Long getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOpen() {
		return startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now());
	}
	
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public Tournament getTournament() {
		return tournament;
	}
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	
	public Set<Submission> getSubmissions() {
		return submissions;
	}
	public void addSubmission(Submission s) {
		submissions.add(s);
	}
	
	public Long getSecondsUntilStartTime() {
		return LocalDateTime.now().until(startDate, ChronoUnit.SECONDS);
	}
	public Long getSecondsUntilEndTime() {
		return LocalDateTime.now().until(endDate, ChronoUnit.SECONDS);
	}
	
	public int hashCode() {
		return this.name == null ? 0 : this.name.hashCode();
	}
	public boolean equals(Object obj) {
		if ( obj instanceof Round ) {
			return ((Round)obj).name == null ? this.name == null : ((Round)obj).name.equals(this.getName());
		}
		return false;
	}
	public void removeProblem(Problem p) {
		problems.remove(p);
		p.removeFromRound(this);
	}
}