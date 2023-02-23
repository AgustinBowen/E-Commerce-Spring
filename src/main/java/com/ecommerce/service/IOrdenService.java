package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Orden;

public interface IOrdenService {
	public List <Orden> findAll();
	public Orden save (Orden orden);
}
