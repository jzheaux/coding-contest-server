package edu.neumont.submission.service;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Language;
import edu.neumont.submission.model.Submission;
import edu.neumont.submission.model.SubmissionResult;

public class SubmissionCallableTest {	
	private edu.neumont.submission.model.Test firstTest = new edu.neumont.submission.model.Test(null, "simple", "1,10,1", "55", 1000, true);
	private edu.neumont.submission.model.Test secondTest = new edu.neumont.submission.model.Test(null, "step", "5,25,4", "90", 1000, true);
	private edu.neumont.submission.model.Test thirdTest = new edu.neumont.submission.model.Test(null, "negative step", "10,1,-1", "55", 1000, true);
	private Set<edu.neumont.submission.model.Test> tests = new HashSet<edu.neumont.submission.model.Test>();
	{
		tests.add(firstTest);
		tests.add(secondTest);
		tests.add(thirdTest);
	}
	private Coder c = new Coder();
	{
		c.setUsername("dave");
	}

    private Submission csharp = new Submission(c, "src/test/resources/submissions/problem/1/james/Solution.cs", null, Language.CSHARP, null, null);

	private Submission s = new Submission();
	{
		s.setOwner(c);
		s.setLocation("src/test/resources/submissions/problem/1/dave/Solution.java");
		s.setLanguage(Language.JAVA);
	}
	
	private Submission kiddie = new Submission();
	{
		kiddie.setOwner(c);
		kiddie.setLocation("src/test/resources/submissions/problem/2/dave/Solution.java");
		kiddie.setLanguage(Language.JAVA);
	}
	
	@Test
	public void testGoodJavaFile() {
		SubmissionCallable sc = new SubmissionCallable(s, tests);
		Submission result = sc.call();
		for ( SubmissionResult sr : result.getResults() ) {
			Assert.assertTrue(sr.isPassed());
		}
	}

    @Test
    public void testGoodCSharpFile() {
	SubmissionCallable sc = new SubmissionCallable(s, tests);
	Submission result = sc.call();
	for ( SubmissionResult sr : result.getResults() ) {
	    Assert.assertTrue(sr.isPassed());
	}
    }

	@Test
	public void testScriptKiddieJavaFile() {
		SubmissionCallable sc = new SubmissionCallable(kiddie, tests);
		Submission result = sc.call();
		for ( SubmissionResult sr : result.getResults() ) {
			Assert.assertFalse(sr.isPassed());
			Assert.assertTrue(sr.getMessages().get(0).contains("AccessControlException"));
		}
	}
}
