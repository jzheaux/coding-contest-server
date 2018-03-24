package edu.neumont.submission.model;

public class BracketPosition {
	private Long id;
	private BracketPosition parent;
	private Round round;
	private Coder coder;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BracketPosition getParent() {
		return parent;
	}
	public void setParent(BracketPosition parent) {
		this.parent = parent;
	}
	public Round getRound() {
		return round;
	}
	public void setRound(Round round) {
		this.round = round;
	}
	public Coder getCoder() {
		return coder;
	}
	public void setCoder(Coder coder) {
		this.coder = coder;
	}
}
