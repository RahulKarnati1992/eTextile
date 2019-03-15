package com.textile.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.Customer;
import com.textile.model.CustomerOrder;

public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long>{

	List<CustomerOrder> findAllByCustomer(Customer customer);
}
