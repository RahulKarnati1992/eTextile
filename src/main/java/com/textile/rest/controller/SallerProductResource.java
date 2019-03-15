package com.textile.rest.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.textile.model.Customer;
import com.textile.model.Product;
import com.textile.model.ProductTag;
import com.textile.repository.ProductTagRepository;
import com.textile.service.CategoryService;
import com.textile.service.CustomerService;
import com.textile.service.ProductService;

@RestController
@RequestMapping("/rest/saller/pd")
public class SallerProductResource {

	private Path path;

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductTagRepository productTagRepository;
	
	@Autowired
	private CustomerService customerService;

	/*
	 * saveProduct.jsp modelAttribute: categoryList(List<Category>),
	 * product(Product), title(String)
	 */
	public void setEditProduct(Product product, Model model) {
		model.addAttribute("categoryList", categoryService.getAllCategory());
		model.addAttribute("product", product);
		model.addAttribute("title", "Edit Product");
	}

	public void setCreateProduct(Product product, Model model) {
		model.addAttribute("categoryList", categoryService.getAllCategory());
		model.addAttribute("product", product);
		model.addAttribute("title", "Add Product");
	}

	@RequestMapping(value = "/s", method = RequestMethod.POST)
	public ResponseEntity<Model> addProductPost(@Valid @RequestBody Product product,
			HttpServletRequest request, Model model) throws IOException {
		
		//customer should add client side only
		/*Long customerId = (Long) request.getSession().getAttribute("customerId_");
		Customer customer = customerService.findOne(customerId);
		
		product.setCustomer(customer);*/

		if (product.getProductId() != null) {
			setEditProduct(product, model);
		} else {
			setCreateProduct(product, model);
		}

		// product image
		MultipartFile productImage = product.getProductImage();
		String rootDirectory = new File("").getAbsolutePath();

		// create product
		if (product.getProductId() == null) {
			productService.save(product);
			File theDir = new File(rootDirectory + "\\src\\main\\webapp\\WEB-INF\\resources\\pics\\"
					+ String.valueOf(product.getProductId()));
			FileUtils.forceMkdir(theDir);
		} else {
			// update product
			productService.save(product);
		}

		path = Paths.get(rootDirectory + "\\src\\main\\webapp\\WEB-INF\\resources\\pics\\" + product.getProductId()
				+ "\\" + "0.png");

		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(path.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Product image saving failed", e);
			}
		}

		// product tags
		if (product.getProductTagsW() != null && !product.getProductTagsW().isEmpty()) {
			List<ProductTag> productTagList = new ArrayList<ProductTag>();
			for (String tag : product.getProductTagsW().split(";")) {
				ProductTag ptag = new ProductTag();
				ptag.setProduct(product);
				ptag.setTagContents(tag);
				productTagRepository.save(ptag);
				productTagList.add(ptag);
			}
			product.setProductTags(productTagList);
		}

		return new ResponseEntity<Model>(model, HttpStatus.CREATED);
	}

	@RequestMapping("/d")
	public ResponseEntity<String> deleteProduct(@RequestParam(value = "id", required = true) Long productId, Model model,
			HttpServletRequest request) {
		String rootDirectory = new File("").getAbsolutePath();
		File theDir = new File(rootDirectory + "\\src\\main\\webapp\\WEB-INF\\resources\\pics\\" + productId);

		if (theDir.exists()) {
			try {
				FileUtils.deleteDirectory(theDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productService.delete(productId);

		return new ResponseEntity<String>("Product Deleted", HttpStatus.OK);
	}

	@RequestMapping(value="/m/{id}", method=RequestMethod.GET)
	public ResponseEntity<List<Product>> productInventory(@PathVariable(value="id") long customerId) {
		
		Customer customer = customerService.findOne(customerId);
		List<Product> products = productService.getAllProductByCustomer(customer);
		
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}

}
