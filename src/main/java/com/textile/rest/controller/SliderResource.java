package com.textile.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.textile.model.Product;
import com.textile.model.Slider;
import com.textile.repository.ProductRepository;
import com.textile.repository.SliderRepository;

@RestController
@RequestMapping("/rest/slider")
public class SliderResource {
	
	@Autowired
	private SliderRepository sliderRepository;
	@Autowired
	private ProductRepository productRepository;
	
	@RequestMapping(value="/all", method=RequestMethod.GET)
	public ResponseEntity<Model> getAllSliders(Model map){
		List<Slider> sliders = (List<Slider>) sliderRepository.findAll();
		List<Product> productPopularList = productRepository.findAll
        		(new PageRequest(0, 8, Direction.DESC, "productViews")).getContent();
		map.addAttribute(sliders);
		map.addAttribute(productPopularList);
		return new ResponseEntity<Model>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Slider> getSlider(@PathVariable(value="id") long sliderId){
		Slider slider = sliderRepository.findOne(sliderId);
		return new ResponseEntity<Slider>(slider, HttpStatus.OK);
		
	}

}
