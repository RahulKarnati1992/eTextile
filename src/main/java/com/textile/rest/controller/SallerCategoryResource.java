package com.textile.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.textile.model.Category;
import com.textile.service.CategoryService;

@RestController
@RequestMapping("/rest/saller/ca")
public class SallerCategoryResource {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/s", method = RequestMethod.POST)
	public ResponseEntity<String> addCategoryPost(@Valid @RequestBody Category category, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<String>("Invalid data", HttpStatus.NOT_ACCEPTABLE);
		}
		categoryService.save(category);
		return new ResponseEntity<String>("Category Created", HttpStatus.CREATED);	
		}
}
