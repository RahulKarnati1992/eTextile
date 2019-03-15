package com.textile.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.textile.model.Product;
import com.textile.model.ProductComment;

public interface ProductCommentRepository extends CrudRepository<ProductComment, Long>{
	List<ProductComment> findByProduct(Product product);
}
