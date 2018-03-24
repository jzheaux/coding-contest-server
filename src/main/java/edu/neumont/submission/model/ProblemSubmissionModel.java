package edu.neumont.submission.model;

public class ProblemSubmissionModel {
	private Submission submission;
	private Coder coder;
	
	public ProblemSubmissionModel(Submission submission, Coder coder) {
		this.submission = submission;
		this.coder = coder;
	}
	
	public String getPreferredLanguage() {
		return coder.getPreferredLanguage().toString();
	}
	
	public String getCode() {
		return submission.getCode();
	}
	
	
}
