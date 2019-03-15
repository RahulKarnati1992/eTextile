package com.textile.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.textile.model.Code;
import com.textile.model.Customer;
import com.textile.repository.CodeRepository;
import com.textile.service.CustomerService;
import com.textile.service.EmailSenderService;

/*
 *	Login/Logout/Register/Retrieve password 
 */
@RestController
@RequestMapping("/rest/user")
public class UserResource {
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private CodeRepository codeRepository;
	
	
	public void setToSession(HttpServletRequest request, Customer customer){
		request.getSession().setAttribute("customerName_", customer.getCustomerName());
        request.getSession().setAttribute("customerId_", customer.getCustomerId());
        request.getSession().setAttribute("cartId_", customer.getCart().getCartId());
	}
	
	@RequestMapping(value = "/rp", method = RequestMethod.POST)
	public ResponseEntity<String> resetPasswordPost(@Valid @RequestBody Customer user, HttpServletRequest request) {
        
        Customer customer = customerService.findByEmail(user.getEmail());
        customer.setPassword(user.getPassword());
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(customer.getEmail(), customer.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        setToSession(request, customer);
        
        customerService.save(customer);
        // delete reset password code in DB
        List<Code> codes = codeRepository.findByCodeTypeAndCustomer(1, customer);
        codeRepository.delete(codes);

        return new ResponseEntity<String>("Password Changed Successfully", HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/rp/{codeStr}", method = RequestMethod.GET)
	public ResponseEntity<Model> resetPassword(@PathVariable String codeStr, Model model){
		Code code = codeRepository.findByCodeStr(codeStr);
		if(code != null){
			Customer customer = code.getCustomer();
			customer.setPassword("");
			model.addAttribute("user",customer);

			return new ResponseEntity<Model>(model, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<Model>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/fp", method = RequestMethod.GET)
	public ResponseEntity<String> findPasswordGet(@RequestParam(value="email", required=false) String email){
		if(email != null){
			Customer customer = customerService.findByEmail(email);
			
			if(customer != null){
				emailSenderService.sendResetPasswordCode(customer);
			}else {
				return new ResponseEntity<String>("User not registered", HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<String>("Email does not exist", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Password Change Link sent to your email", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rg/{codeStr}", method = RequestMethod.GET)
	public ResponseEntity<String> activeAccount(@PathVariable String codeStr){
		customerService.activeAccount(codeStr);
		return new ResponseEntity<String>("Account activated successfully, Continue shopping with us", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Model> registerCustomerPost(@Valid @RequestBody Customer user, BindingResult result,
			HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		// if user already login, then redirect to home page.
		if(session.getAttribute("customerName_") != null){
			String msg= "Already registered";
			model.addAttribute(msg);
			return new ResponseEntity<Model>(model, HttpStatus.ALREADY_REPORTED);
		}
		
        /*if(result.hasErrors()){
            return "registerCustomer";
        }*/
        if(customerService.findByEmail(user.getEmail())!= null){
        	String msg= "Email already Exists";
			model.addAttribute(msg);
        	return new ResponseEntity<Model>(model, HttpStatus.ALREADY_REPORTED);
        }

        customerService.save(user);
        Customer customer = customerService.findByEmail(user.getEmail());
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        setToSession(request, customer);
        
        emailSenderService.sendActiveCode(customer);
        String msg= "Email already Exists";
		model.addAttribute(msg);
		model.addAttribute(customer);
        return new ResponseEntity<Model>(model, HttpStatus.CREATED);
    }
	
	
	@RequestMapping("/login")
	public ResponseEntity<String> login(@RequestParam(value = "error", required = false) String error,
			HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		// if user already login, then redirect to home page.
		if(session.getAttribute("customerName_") != null){
			return new ResponseEntity<String>("Already Logged-In", HttpStatus.ALREADY_REPORTED);
		}
    	if (error != null) {
    		return new ResponseEntity<String>("Invalid Username and Password", HttpStatus.NOT_FOUND);
		}
    	return new ResponseEntity<String>("Logged-In Successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public ResponseEntity<String> logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return new ResponseEntity<String>("Logged-Out Successfully", HttpStatus.OK);
	}
	  
}
