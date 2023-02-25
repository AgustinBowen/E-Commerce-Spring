package com.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.model.DetalleOrden;
import com.ecommerce.model.Orden;
import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.IDetalleOrdenService;
import com.ecommerce.service.IOrdenService;
import com.ecommerce.service.IUsuarioService;
import com.ecommerce.service.ProductoServices;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoServices productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	Orden orden = new Orden();
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		log.info("Session del usuario: {}",session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parametro {}",id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		
		//validar que no se añada el mismo producto x cantidad de veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		
		
		return "usuario/carrito";
	}
	
	//Quitar un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id,Model model) {
		
		//nueva lista con -1 elemento que es el eliminado 
		List<DetalleOrden> detallesNuevos = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				detallesNuevos.add(detalleOrden);
			}
		}
		
		detalles = detallesNuevos;
		
		double sumaTotalNueva = 0;
		
		sumaTotalNueva = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotalNueva);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order (Model model, HttpSession session){
		
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		
		return "usuario/resumenorden";
	}
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		
		Date fecha = new Date();
		orden.setFechaCreacion(fecha);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//usuario
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//guardar detalles
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		//limpiar los detalles y la orden
		orden = new Orden();
		detalles.clear();
		
		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProduct (@RequestParam String nombre, Model model) {
		List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase())).collect(Collectors.toList());
		model.addAttribute("productos", productos);	
		return "usuario/home";
	}
}
