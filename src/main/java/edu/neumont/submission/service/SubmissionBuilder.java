package edu.neumont.submission.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Language;
import edu.neumont.submission.model.Problem;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Submission;
import edu.neumont.submission.model.SubmissionUploadException;

public class SubmissionBuilder {
	private String source;
	private Coder owner;
	private Language language;
	private String location;
	private Long id;
	private Problem problem;
	private Round round;
	
	public SubmissionBuilder(String source) {
		//this.source = ;
		this.source = source;
	}

	public SubmissionBuilder owner(Coder owner) {
		this.owner = owner;
		return this;
	}
	
	public SubmissionBuilder language(Language language) {
		this.language = language;
		return this;
	}
	
	public SubmissionBuilder location(String location) {
		this.location = location;
		return this;
	}
	
	public SubmissionBuilder problem(Problem problem) {
		this.problem = problem;
		return this;
	}
	
	public SubmissionBuilder round(Round round) {
		this.round = round;
		return this;
	}
	
	public SubmissionBuilder fromExisting(Submission s) {
		language = s.getLanguage();
		location = new File(s.getLocation()).getName();
		id = s.getId();
		problem = s.getProblem();
		round = s.getRound();
		owner = s.getOwner();
		return this;
	}
	
	public Submission build() {
		File directory = new File("submissions/problem/" + problem.getId() + "/" + owner.getUsername());
		try ( FileOutputStream fos = FileOutputStreamFactory.create(directory, location) ) {
			IOUtils.copy(new ByteArrayInputStream(source.getBytes()), fos);
		} catch ( IOException e ) {
			throw new SubmissionUploadException(String.format("Failed to upload submission %s for problem %s", location, problem.getId()), e);
		}
		File file = new File(directory, location);
		
		return new Submission(id, owner, file.getAbsolutePath(), source, language, problem, round);
	}
	
	private static class FileOutputStreamFactory {
		public static FileOutputStream create(File directory, String fileName) throws IOException {
			directory.mkdirs();
			return new FileOutputStream(new File(directory, fileName));
		}
	}
}
