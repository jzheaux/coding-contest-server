package edu.neumont.submission.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="coder")
@NamedQueries({
	@NamedQuery(name="byUsername", query="SELECT u FROM Coder u WHERE u.username = :username"),
	@NamedQuery(name="findAll", query="SELECT u FROM Coder u")
})
public class Coder implements Serializable , Comparable<Coder> {
	@Id
	@Column
	@GeneratedValue(generator="seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq", sequenceName="seq")
	private Long id;
	
	@Size(min=1, message="First Name cannot be empty")
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Size(min=1, message="Last Name cannot be empty")
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Size(min=1, message="Email cannot be empty")
	@Pattern(regexp="[A-Za-z0-9\\.\\-_\\+]+@[A-Za-z0-9\\-_\\+]+\\.[A-Za-z0-9\\.\\-_\\+]+", message="Unsupported email format.")
	@Column(unique=true)
	private String email;
	
	@Size(min=1, message="Username cannot be empty")
	@Pattern(regexp="[A-Za-z0-9\\.\\-_]+", message="Please use only alphanumerics, dots, hyphens, and dashes.")
	@Column(unique=true)
	private String username;
	
	@Size(min=1, message="Password cannot be empty")
	@Password
	@Column
	private char[] password;
	
	@Size(min=1, message="Confirm cannot be empty")
	@Password
	@Transient
	private char[] confirmPassword;
	
	@Enumerated
	@NotNull
	@Column(name="PREFERRED_LANGUAGE")
	private Language preferredLanguage;
	
	@NotNull(message="Age cannot be empty")
	@Min(16)
	@Max(99)
	@Column
	private Integer age;
	
	@NotNull(message="Graduation Year cannot be empty")
	@Min(2014)
	@Max(2050)
	@Column(name="GRADUATION_YEAR")
	private Integer graduationYear;
	
	@Column(name="IMAGE_LOCATION")
	private String imageLocation;
	
	@Size(max=300)
	@Pattern(regexp="[A-Za-z0-9\\s\\.\\-_\\+=!@#\\$%\\^\\*\\(\\):;\"'\\?,\\/\\[\\]]+", message="Please enter a short bio and do not include any html tags.")
	@Column
	private String bio;
	
	@ManyToMany(mappedBy="coders")
	private Set<Tournament> tournaments = new HashSet<Tournament>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getName() {
		return firstName + " " + lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public char[] getPassword() {
		return password;
	}
	public String getPasswordAsString() {
		return password == null ? null : new String(password);
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public Language getPreferredLanguage() {
		return preferredLanguage;
	}
	public void setPreferredLanguage(Language preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getGraduationYear() {
		return graduationYear;
	}
	public void setGraduationYear(Integer graduationYear) {
		this.graduationYear = graduationYear;
	}
	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public char[] getConfirmPassword() {
		return confirmPassword;
	}
	public String getConfirmPasswordAsString() {
		return confirmPassword == null ? null : new String(confirmPassword);
	}
	public void setConfirmPassword(char[] confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof Coder ) {
			Coder that = (Coder)obj;
			return that.username == null ? this.username == null : that.username.equals(this.username);
		}
		return false;
	}
	
	public int hashCode() {
		return this.username == null ? super.hashCode() : this.username.hashCode();
	}

	public void addTournament(Tournament tournament) {
		tournaments.add(tournament);
	}
	
	public void removeTournament(Tournament tournament) {
		tournaments.remove(tournament);
	}
	
	@Override
	public int compareTo(Coder o) {
		return this.getUsername().compareTo(o.getUsername());
	}
}