package com.textile.repository;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.CustomerOrderShippingAddress;

public interface CustomerOrderShippingAddressRepository 
					extends CrudRepository <CustomerOrderShippingAddress, Long> {

}
