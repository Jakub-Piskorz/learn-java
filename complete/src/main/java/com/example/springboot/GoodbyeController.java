package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodbyeController {

	@GetMapping("/firstname/{firstName}/{lastName}")
	public String index(@PathVariable String firstName, String lastName) {
		System.out.println(firstName);
		System.out.println(lastName);
		return "Bye!";
	}

}
