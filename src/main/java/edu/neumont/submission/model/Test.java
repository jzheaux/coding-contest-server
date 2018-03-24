package edu.neumont.submission.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="TEST")
public class Test {
	@Id
	@Column
	@GeneratedValue(generator="seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq", sequenceName="seq")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="PROBLEM_ID")
	private Problem problem;
	
	@Column
	private String name;
	
	@Column
	private String input;
	
	@Column
	private String expected;
	
	@Column(name="MAX_TIME")
	private long maxTime;

	@Column(name="IS_PUBLIC")
	private boolean isPublic;
	
	public Test() {}
	
	public Test(Problem problem, String name, String input, String expected, long maxTime, boolean isPublic) {
		this.problem = problem;
		this.name = name;
		this.input = input;
		this.expected = expected;
		this.maxTime = maxTime;
		this.isPublic = isPublic;
	}

	public Long getId() {
		return id;
	}
	
	public Problem getProblem() {
		return problem;
	}

	public String getName() {
		return name;
	}

	public String getInput() {
		return input;
	}

	public String getExpected() {
		return expected;
	}

	public long getMaxTime() {
		return maxTime;
	}
	
	public boolean isPublic() {
		return isPublic;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expected == null) ? 0 : expected.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + (isPublic ? 1231 : 1237);
		result = prime * result + (int) (maxTime ^ (maxTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Test other = (Test) obj;
		if (expected == null) {
			if (other.expected != null)
				return false;
		} else if (!expected.equals(other.expected))
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (isPublic != other.isPublic)
			return false;
		if (maxTime != other.maxTime)
			return false;
		return true;
	}
}
