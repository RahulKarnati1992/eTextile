package com.textile.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.Code;
import com.textile.model.Customer;

public interface CodeRepository extends CrudRepository<Code, Long>{
	
	List<Code> findByCodeTypeAndCustomer(int codeType, Customer customer);
	
	Code findByCodeStr(String codeStr);
}
