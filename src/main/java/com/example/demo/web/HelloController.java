package com.example.demo.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/greeting", method = RequestMethod.GET)
@RestController
public class HelloController {
	
	@RequestMapping("/basic") //default method is GET
	public String sayHello() {
		return "<h1>Hello World!</h1>";
	}
	
	@RequestMapping(value = "/proper", method = RequestMethod.GET)
	public String sayProperHello() {
		return "<h1>Hello there, how are you ?</h1>";
	}
	
	@RequestMapping(value = "/user_entry", method = RequestMethod.GET)
	public String userForm() {
		return "<form action=\"/greeting/user_greeting\" method=\"GET\">\r\n"
				+ "  <label for=\"fname\">First name:</label><br>\r\n"
				+ "  <input type=\"text\" id=\"fname\" name=\"fname\"><br>\r\n"
				+ "  <label for=\"lname\">Last name:</label><br>\r\n"
				+ "  <input type=\"text\" id=\"lname\" name=\"lname\">\r\n"
				+ "  <input type=\"submit\" value=\"Submit\">\r\n"
				+ "</form>";
	}
	
	@RequestMapping(value = "/user_greeting", method = RequestMethod.GET)
	public String printUserGreeting(@RequestParam String fname, @RequestParam String lname) {
		return "Hello there, " + fname + " " + lname;
	}
	
	@RequestMapping(value="/orders/{id}", method = RequestMethod.GET)
	public String getOrder(@PathVariable String id) {
		return "Order ID: " + id;
	}
	
}
