package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.service.ProductoServices;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private ProductoServices productosService;
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("productos", productosService.findAll());
		return "usuario/home";
	}
}
