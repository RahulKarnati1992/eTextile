package com.textile.service;

import java.io.IOException;
import java.util.List;

import com.textile.model.Cart;
import com.textile.model.Customer;
import com.textile.model.CustomerOrder;
import com.textile.model.CustomerOrderShippingAddress;

public interface CustomerOrderService {
	
	double getCustomerOrderGrandTotalByCart(Cart cart);
	
	void addOrderDumpCart(CustomerOrderShippingAddress customerOrderShippingAddress, CustomerOrder customerOrder, Cart cart) throws IOException;
	
	List<CustomerOrder> getAllCustomerOrderByCustomer(Customer customer);
}
