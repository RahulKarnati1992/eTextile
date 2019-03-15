package com.textile.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.Customer;
import com.textile.model.ShippingAddress;

public interface ShippingAddressRepository extends CrudRepository<ShippingAddress, Long>{
	
	List<ShippingAddress> findAllByCustomer(Customer customer);
	
	
}
