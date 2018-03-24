package edu.neumont.submission.auth;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.model.Round;
import edu.neumont.submission.model.Tournament;
import edu.neumont.submission.service.CoderUserDetailsService.CoderUser;

public abstract class Authorized {
	public static final Predicate<Tournament> tournamentAccessPredicate = (t) -> {
		CoderUser current = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return current.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || t.isPublic() || t.getCoders().contains(current.getCoder());
	};
	
	public static <T> T withCoder(Coder c, Supplier<T> hasAccess) {
		CoderUser current = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( current.getCoder().equals(c) || current.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
			return hasAccess.get();
		}
		
		return null;
	}
	
	public static <T> T withCoder(Coder c, Supplier<T> hasAccess, Supplier<T> hasntAccess) {
		CoderUser current = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ( current.getCoder().equals(c) ) {
			return hasAccess.get();
		} else {
			return hasntAccess.get();
		}
	}
	
	public static <T> T withTournament(Tournament t, Supplier<T> hasAccess, Supplier<T> hasntAccess) {
		if ( tournamentAccessPredicate.test(t) ) {
			return hasAccess.get();
		} else {
			return hasntAccess.get();
		}
	}
	
	public static <T> T withRound(Round r, Supplier<T> hasAccess) {
		CoderUser current = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return 
			withTournament(r.getTournament(),
				() -> {
					if ( r.isOpen()|| current.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
						return hasAccess.get();
					} else {
						return null;
					}
				},
				() -> null);
	}
	
	public static <T> T withRound(Round r, Supplier<T> hasAccess, Supplier<T> hasntAccess) {
		CoderUser current = (CoderUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return 
			withTournament(r.getTournament(),
				() -> {
					if ( r.isOpen()|| current.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
						return hasAccess.get();
					} else {
						return hasntAccess.get();
					}
				},
				() -> hasntAccess.get());
	}
}
