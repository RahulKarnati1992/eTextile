package com.textile.repository;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.Customer;
import com.textile.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{
	Role findByAuthorityAndCustomer(String auth, Customer customer);
}
