package com.ecommerce.model;

public class DetalleOrden {
	private Integer id;
	private String nomnbre;
	private double cantidad;
	private double precio;
	private double total;
	
	public DetalleOrden() {

	}

	public DetalleOrden(Integer id, String nomnbre, double cantidad, double precio, double total) {
		this.id = id;
		this.nomnbre = nomnbre;
		this.cantidad = cantidad;
		this.precio = precio;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomnbre() {
		return nomnbre;
	}

	public void setNomnbre(String nomnbre) {
		this.nomnbre = nomnbre;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "DetalleOrden [id=" + id + ", nomnbre=" + nomnbre + ", cantidad=" + cantidad + ", precio=" + precio
				+ ", total=" + total + "]";
	}
	
}
