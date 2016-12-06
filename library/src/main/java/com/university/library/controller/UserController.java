package com.university.library.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.university.library.model.SecUserDetails;
import com.university.library.model.User;
import com.university.library.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = { "/demands" }, method = RequestMethod.GET)
	public String openPage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getName());
		model.addAttribute("userBooks", (user.getBooks()));
		return "demands";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping(value = "/register")
	public String signupPage(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username != null && !"".equals(username)) {
			User user = userRepository.findByUsername(username);
			if (user != null) {
				return "redirect:/register?error=This username is already taken, please choose another";
			} else {
				if (!"".equals(password)) {
					// save new user to the database
					User registerUser = new User(username, password);
					userRepository.save(registerUser);

					// authenticate user
					SecUserDetails secureUser = new SecUserDetails(registerUser);
					Authentication auth = new UsernamePasswordAuthenticationToken(secureUser, null, null);
					SecurityContextHolder.getContext().setAuthentication(auth);
					return "redirect:demands";
				} else {
					return "redirect:/register?error=Please specify the password";
				}
			}
		} else {
			if (password != null && !"".equals(password)) {
				return "redirect:/register?error=Please specify username";
			}
		}

		return "register";
	}
}
