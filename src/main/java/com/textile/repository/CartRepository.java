package com.textile.repository;

import javax.transaction.Transactional;


import org.springframework.data.repository.CrudRepository;

import com.textile.model.Cart;


@Transactional
public interface CartRepository extends CrudRepository<Cart, Long>{
	
}
