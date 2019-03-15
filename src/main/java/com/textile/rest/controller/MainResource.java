package com.textile.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.textile.model.Product;
import com.textile.model.Slider;
import com.textile.repository.CartRepository;
import com.textile.repository.ProductRepository;
import com.textile.repository.SliderRepository;
import com.textile.service.CategoryService;

@RestController
@RequestMapping("/rest/home")
public class MainResource {
	
	@Autowired
	private SliderRepository sliderRepository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CartRepository cartRepository;
	
	@RequestMapping(value = "/hi", method = RequestMethod.GET)
	public String sayHi() {
		return "Hi";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Model> home(Model model) {
		
		List<Slider> sliderList = (List<Slider>) sliderRepository.findAll();
        List<Product> productList = productRepository.findAll(new PageRequest(0, 16)).getContent();
        List<String> mainCategoryNameList = categoryService.getAllMainCategory();
        List<Product> productPopularList = productRepository.findAll
        		(new PageRequest(0, 8, Direction.DESC, "productViews")).getContent();
        List<Product> productLatest = productRepository.findAll
        		(new PageRequest(0, 8, Direction.DESC, "productDate")).getContent();

        model.addAttribute("productPopular", productPopularList);
        model.addAttribute("productLatest", productLatest);
        model.addAttribute("sliders", sliderList);
        model.addAttribute("products", productList);
        model.addAttribute("mainCategoryNameList", mainCategoryNameList);
        return new ResponseEntity<Model>(model, HttpStatus.OK);
	}
	
	

}
