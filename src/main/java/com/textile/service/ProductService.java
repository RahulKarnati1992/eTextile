package com.textile.service;

import java.util.List;

import com.textile.model.Category;
import com.textile.model.Customer;
import com.textile.model.Product;

public interface ProductService {
	
	void getVisited(Long productId);
	
	List<Product> getProductsByMainCategory(String mainCategoryName);
	
	List<Product> getProductsByCategory(Category category);
	
	List<Product> getAllProducts();
	
	Product getProductById(Long productId);
	
	void save(Product product);
	
	void delete(Long productId);
	
	List<Product> sort(List<Product> products, String sortType);
	
	List<Product> getAllProductByCustomer(Customer customer);
}
