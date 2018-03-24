package edu.neumont.submission.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.neumont.submission.model.Coder;

@Service
public class CoderUserDetailsService implements UserDetailsService {
	@Inject CoderService coderService;

	private final List<String> adminUsernames = Arrays.asList("jcummings", "jkrebs", "mwarner", "shalladay", "tnichols");
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Coder c = coderService.getByUsername(username);
		
		if ( c != null ) {
			Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
			if ( adminUsernames.contains(username) ) {
				authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}
			
			UserDetails ud = new CoderUser(c, authorities);
			
			return ud;
		}
		
		throw new UsernameNotFoundException("Username not found.");
	}

	public static class CoderUser extends User {
		private static final long serialVersionUID = 1L;
		
		private Coder coder;
		
		public CoderUser(Coder c, Collection<? extends GrantedAuthority> authorities) {
			super(c.getUsername(), new String(c.getPassword()), authorities);
			coder = c;
		}
		
		public Coder getCoder() {
			return coder;
		}
	}
}
