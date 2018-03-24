package edu.neumont.submission.config;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Inject UserDetailsService userDetailsService;
	
	@Inject
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder("secret");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//			.requiresChannel().anyRequest().requiresSecure()
//			.and()
		.authorizeRequests()
			.antMatchers("/register", "/resources/**").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.successHandler(successHandler())
			.permitAll()
			.and()
		.logout()
			.permitAll();
	}
	
	protected AuthenticationSuccessHandler successHandler() {
		RedirectStrategy rs = new RedirectStrategy() {

			@Override
			public void sendRedirect(HttpServletRequest request,
					HttpServletResponse response, String url)
					throws IOException {
				String contextPath = StringUtils.defaultIfEmpty(request.getContextPath(), "/");
				
				url = url.substring(url.lastIndexOf("://") + 3); // strip off scheme
		        /*url = url.substring(url.indexOf(contextPath) + contextPath.length());

		        if (url.length() > 1 && url.charAt(0) == '/') {
		            url = url.substring(1);
		        }*/
		        
		        response.sendRedirect("https://" + url);
			}
			
		};
		
		SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
		handler.setRedirectStrategy(rs);
		
		return handler;
	}
	
	/*@Configuration
	protected static class AuthenticationConfiguration extends
			GlobalAuthenticationConfigurerAdapter {
		
		@Inject UserDetailsService userDetailsService;
		
		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.ldapAuthentication()
					.ldapAuthoritiesPopulator(new UserDetailsServiceLdapAuthoritiesPopulator(userDetailsService))
					.userDnPatterns("uid={0},ou=people")
					.groupSearchBase("ou=groups")
					.contextSource().ldif("classpath:test-server.ldif");
		}
	}*/
}
