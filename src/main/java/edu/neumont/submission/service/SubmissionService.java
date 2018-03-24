package edu.neumont.submission.service;
import edu.neumont.submission.model.Language;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;


public interface SubmissionService {
	Submission getSubmission(Long submissionId);
	Submission getSubmission(Round r, Problem p);
	
	Submission postSubmission(Round r, Problem p, String fileName, Language language, String source);
	Submission testSubmission(Round r, Problem p, String fileName, Language language, String source);
	Submission putSubmission(Long submissionId, String fileName, Language language, String source);
	
	void deleteSubmission(Long submissionId);
}
