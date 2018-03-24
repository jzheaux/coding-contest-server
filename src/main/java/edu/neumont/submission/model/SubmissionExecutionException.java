package edu.neumont.submission.model;

public class SubmissionExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SubmissionExecutionException(String message, Throwable t) {
		super(message, t);
	}

}
