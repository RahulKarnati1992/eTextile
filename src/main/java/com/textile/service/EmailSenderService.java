package com.textile.service;

import com.textile.model.Customer;
import com.textile.model.CustomerOrder;

public interface EmailSenderService {

	void sendActiveCode(Customer customer);
	
	void sendResetPasswordCode(Customer customer);
	
	void sendOrderDetails(CustomerOrder customerOrder);
}
