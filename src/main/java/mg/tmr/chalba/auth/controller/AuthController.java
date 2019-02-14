package mg.tmr.chalba.auth.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import mg.tmr.chalba.auth.auth.service.UserService;
import mg.tmr.chalba.auth.model.User;

@Controller
public class AuthController {
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private UserService userService;
	
	@Autowired
	public AuthController(BCryptPasswordEncoder bCryptPasswordEncoder, 
			UserService userService) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
	}
	
	// SHOW LOGIN PAGE
	@GetMapping(value= {"/auth/login", "/login", "/"})
	public ModelAndView showLoginPage(ModelAndView modelAndView, User user){
		modelAndView.addObject("user", user);
		modelAndView.setViewName("views/auth/login");
		return modelAndView;
	}
	
	// SHOW REGISTRATION FORM
	@GetMapping(value="/auth/register")
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user){
		modelAndView.addObject("user", user);
		modelAndView.setViewName("views/auth/register");
		return modelAndView;
	}
	
	// PROCESS REGISTRATION FORM
	@PostMapping(value="/auth/register")
	public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, 
			@RequestParam Map<String, String> requestParams, HttpServletRequest request) {
		
		// Lookup user in database
		User userNameExists = null;
		if (user.getUserName() != null) {
			userNameExists = userService.findByUserName(user.getUserName());
		}
				
		if (userNameExists != null) {
			
			modelAndView.addObject("alreadyRegisteredMessage", "Oops!  There is already a user registered with the username provided.");
			modelAndView.setViewName("views/auth/register");
			bindingResult.reject("userName");
		}
		
		if (bindingResult.hasErrors()) { 
			
			//===================================================
			System.out.println("===================================");
			System.out.println("\n\n" + bindingResult.getAllErrors() + "\n\n");
			System.out.println("===================================");
		    //===================================================   
			
			modelAndView.setViewName("views/auth/register");
			
		} else { // new user so we create user and send confirmation e-mail
					
			// Disable user until they click on confirmation link in email
		    //user.setEnabled(false);
		      
		    // Generate random 36-character string token for confirmation link
			user.setConfirmationToken(UUID.randomUUID().toString());
		    
		    Zxcvbn passwordCheck = new Zxcvbn();
			
			Strength strength = passwordCheck.measure(requestParams.get("password"));
			
			if (strength.getScore() < 3) {
				//modelAndView.addObject("errorMessage", "Your password is too weak.  Choose a stronger one.");
				bindingResult.reject("password");
				
				modelAndView.setViewName("redirect:confirm?token=" + requestParams.get("token"));
				System.out.println(requestParams.get("token"));
				return modelAndView;
			}
		
			user.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));

			// Set user to enabled
			user.setEnabled(true);
			
			userService.saveUser(user);
				
			modelAndView.addObject("confirmationMessage", "Registration Successfull. You Can Login");
			modelAndView.setViewName("views/auth/login");
			
		}
			
		return modelAndView;
	}
	
	// Return registration form template
	@GetMapping(value="/no_role")
	public ModelAndView showNoRolePage(ModelAndView modelAndView, User user){
		modelAndView.addObject("user", user);
		modelAndView.setViewName("views/dash/no_role");
		return modelAndView;
	}

}
