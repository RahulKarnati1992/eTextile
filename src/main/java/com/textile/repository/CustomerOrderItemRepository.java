package com.textile.repository;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.CustomerOrderItem;

public interface CustomerOrderItemRepository extends CrudRepository <CustomerOrderItem, Long>{

}
