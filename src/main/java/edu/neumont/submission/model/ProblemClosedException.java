package edu.neumont.submission.model;

public class ProblemClosedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProblemClosedException(String message) {
		super(message);
	}
}
