package edu.neumont.submission.controller;

import java.util.Arrays;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.service.CoderService;

@Controller
public class RegisterController {
	@Inject private CoderService coderService;
	
	@RequestMapping("/register")
	public String beginRegistrationWorkflow(@ModelAttribute("model") Coder coder) {
		return "/coder/edit";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String commitRegistrationWorkflow(@ModelAttribute("model") @Valid Coder coder, BindingResult bindingResult) {
		if ( !Arrays.equals(coder.getPassword(), coder.getConfirmPassword()) ) {
			bindingResult.rejectValue("confirmPassword", "confirmPassword.mismatch", "Password and Confirm Password do not match.");
		}
		
		if ( bindingResult.hasErrors() ) {
			return "/coder/edit";
		} else {
			try {
				coderService.addCoder(coder);
			} catch ( JpaSystemException cve ) {
				// coder already exists
				bindingResult.rejectValue("username", "username.taken", "Username or Email Address already taken, please specify another.");
				return "/coder/edit";
			}

			return "redirect:/";
		}
	}
}
