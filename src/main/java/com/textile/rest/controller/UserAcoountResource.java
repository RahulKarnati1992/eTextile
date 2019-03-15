package com.textile.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.textile.model.Customer;
import com.textile.model.CustomerOrder;
import com.textile.model.ShippingAddress;
import com.textile.service.CustomerAddressService;
import com.textile.service.CustomerOrderService;
import com.textile.service.CustomerService;
import com.textile.service.EmailSenderService;

@RestController
@RequestMapping("/rest/customer")
public class UserAcoountResource {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private CustomerAddressService customerAddressService;
	@Autowired
    private CustomerOrderService customerOrderService;

	@RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
	public ResponseEntity<Model> account(@PathVariable(value = "id") Long customerId, HttpServletRequest request, Model model) {

		Customer customer = customerService.findOne(customerId);
		model.addAttribute("customer", customer);

//	        List<ShippingAddress> customerBillingAddresses = customerAddressRepository.findByIsShippingAndCustomer(false, customer);
		List<ShippingAddress> customerShippingAddresses = customerAddressService
				.getAllShippingAddressByCustomerId(customerId);
		/*
		 * for(ShippingAddress defaultBillingAddress : customerBillingAddresses){
		 * if(defaultBillingAddress.getIsDefault()){
		 * model.addAttribute("billingAddress",customerBillingAddresses); } }
		 */
		for (ShippingAddress defaultShippingAddress : customerShippingAddresses) {
			if (defaultShippingAddress.getIsDefault()) {
				model.addAttribute("shippingAddress", defaultShippingAddress);
				break;
			}
		}

		if (customerService.hasRole("ROLE_UNAUTH", customer)) {
			model.addAttribute("unAuth", "unAuth");
		}

		// Get Customer Orders
		List<CustomerOrder> customerOrders = customerOrderService.getAllCustomerOrderByCustomer(customer);
		model.addAttribute("customerOrders", customerOrders);

		return new ResponseEntity<Model>(model, HttpStatus.OK);
	}

	@RequestMapping(value = "/rsac/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> resendActiveCode(@PathVariable(value="id") long customerId, HttpServletRequest request) {
		Customer customer = customerService.findOne(customerId);
		if (customer != null) {
			if (!customerService.hasRole("ROLE_USER", customer))
				emailSenderService.sendActiveCode(customer);
		}
		return new ResponseEntity<String>("Activation code sent to your email", HttpStatus.OK);
	}

}
