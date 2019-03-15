package com.textile.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.CartItem;



@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long>{

}
