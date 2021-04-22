package com.suraj.loginregistration.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.suraj.loginregistration.models.Client;
import com.suraj.loginregistration.services.ClientService;
import com.suraj.loginregistration.validators.ClientValidator;

@Controller
public class MainController {
	
	 private final ClientValidator clientValidator;
	//connect our Service and constuct our Controller
	private final ClientService clientService;
	public MainController(ClientService clientService, ClientValidator clientValidator) {
		this.clientService = clientService;
		this.clientValidator = clientValidator;
	}
	
	//render and catch form info
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("client", new Client());
		return "index.jsp";
	}
	
	//process form info
	@RequestMapping(value="/registration", method=RequestMethod.POST)
	public String registration(@Valid @ModelAttribute("client") Client client, BindingResult result, HttpSession session) {
		clientValidator.validate(client, result);
		if (result.hasErrors()) {
			return "index.jsp";
		}
		else {
//			if(clientService.findByEmail(client.getEmail())) {
//				return "redirect:/";
//			}
			Client c = clientService.registerClient(client);
			// put client Id into session
			session.setAttribute("clientId", c.getId());
			return "redirect:/dashboard";
		}

	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password,
			HttpSession session,
			RedirectAttributes redirect
			) {
		if(clientService.authenticateClient(email, password)) {
			Client c = clientService.findByEmail(email);
			//put 'clientId' in session
			session.setAttribute("clientId", c.getId());
			return "redirect:/dashboard";
		}
		else {
			redirect.addFlashAttribute("error", "email or password don't match");
			return "redirect:/";
		}

	}
	/////////////// Home/Dashboard  /////////////////////
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model, HttpSession session, RedirectAttributes redirect) {
		Long clientId = (Long) session.getAttribute("clientId");
		if(clientId == null) {
			redirect.addFlashAttribute("caution", "Please Register or Login before proceeding.");
			return "redirect:/";
		}
		
		model.addAttribute("client", clientService.findClientById(clientId));
		return "dashboard.jsp";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
//
}
