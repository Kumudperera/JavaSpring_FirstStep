package com.example.demo.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Products;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/{userId}")
	public String displayUser(@PathVariable int userId) {
		return "User Found: " + userId;
	}
	
	@RequestMapping("/{userId}/invoices")
	public String displayUserInvoices(@PathVariable("userId") int userId, @RequestParam(value = "date", required = false) Date dateOrNull) {
		return "Invoice found for user: " + userId + " on the date: " + dateOrNull;
	}
	
	@RequestMapping("/{userId}/items")
	public List<String> displayStringJson() {
		return Arrays.asList("Shoes", "laptop", "button");
	}
	
	@RequestMapping("/{userId}/product_as_json")
	public List<Products> displayProductsJson() {
		return Arrays.asList(new Products(1, "Shoes", 259.92), 
				new Products(2, "Books", 209.89), 
				new Products(3, "Bag", 23.90));
	}
}
