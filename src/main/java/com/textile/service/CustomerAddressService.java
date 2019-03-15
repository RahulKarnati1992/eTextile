package com.textile.service;

import java.util.List;

import com.textile.model.ShippingAddress;

public interface CustomerAddressService {
	
	List<ShippingAddress> getAllShippingAddressByCustomerId(Object customerId);
	
	void addShippingAddressObject(Object customerId, ShippingAddress shippingAddress);
}
