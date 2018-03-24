package edu.neumont.submission.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.neumont.submission.model.Coder;
import edu.neumont.submission.service.CoderService;

@Controller
public class ProfileController {
	@Inject CoderService coderService;
	
	@RequestMapping("/coder/{id}")
	public ModelAndView viewProfile(@PathVariable("id") Long id) {
		Coder coder = coderService.getCoder(id);
		return new ModelAndView("/coder/view", "model", coder);
	}
}
