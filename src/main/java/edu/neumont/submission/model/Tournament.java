package edu.neumont.submission.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries(@NamedQuery(name="findAllActive", query="FROM Tournament WHERE isActive = 1"))
@Entity
@Table(name="tournament")
public class Tournament {
	@Id
	@Column
	@GeneratedValue(generator="seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq", sequenceName="seq")
	private Long id;
	
	@Column
	private String name;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="tournament")
	private Set<Round> rounds = new HashSet<Round>();
	
	@Column(name="IS_ACTIVE")
	private boolean isActive;
	
	@Column(name="IS_PUBLIC")
	private boolean isPublic;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="TOURNAMENT_CODER",
		joinColumns=@JoinColumn(name="TOURNAMENT_ID", nullable=false),
		inverseJoinColumns=@JoinColumn(name="CODER_ID", nullable=false))
	private Set<Coder> coders = new HashSet<Coder>(); 
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Round> getRounds() {
		return rounds;
	}
	public void addRound(Round round) {
		rounds.add(round);
		round.setTournament(this);
	}
	public void removeRound(Round round) {
		rounds.remove(round);
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public Round getNextRound() {
		Round next = null;
		for ( Round round : rounds ) {
			if ( round.getStartDate().isBefore(LocalDateTime.now()) &&
					round.getEndDate().isAfter(LocalDateTime.now()) ) {
				return round;
			} else if ( round.getStartDate().isAfter(LocalDateTime.now()) && ( next == null || next.getStartDate().isAfter(round.getStartDate()) ) ) {
				next = round;
			}
		}
		return next;
	}

	public Round getRound(Long roundId) {
		for ( Round round : rounds ) {
			if ( round.getId().equals(roundId) ) {
				return round;
			}
		}
		return null;
	}
	
	public void addCoder(Coder coder) {
		coders.add(coder);
		coder.addTournament(this);
	}
	
	public void removeCoder(Coder coder) {
		coders.remove(coder);
		coder.removeTournament(this);
	}
	
	public Set<Coder> getCoders() {
		return Collections.unmodifiableSet(coders);
	}
	
	public LocalDateTime getStartDate() {
		LocalDateTime earliest = null;
		for ( Round round : rounds ) {
			if ( earliest == null || earliest.isAfter(round.getStartDate()) ) {
				earliest = round.getStartDate();
			}
		}
		return earliest;
	}
	
	public LocalDateTime getEndDate() {
		LocalDateTime latest = null;
		for ( Round round : rounds ) {
			if ( latest == null || latest.isBefore(round.getEndDate()) ) {
				latest = round.getEndDate();
			}
		}
		return latest;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof Tournament ) {
			Tournament that = (Tournament)obj;
			return that.id == null ? this.id == null : that.id.equals(this.id);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id == null ? 0 : this.id.hashCode();
	}
	
}
