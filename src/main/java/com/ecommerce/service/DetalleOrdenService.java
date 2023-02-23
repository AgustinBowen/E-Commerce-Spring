package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.DetalleOrden;
import com.ecommerce.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenService implements IDetalleOrdenService{

	@Autowired
	private IDetalleOrdenRepository detalleRepository;
	
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		return detalleRepository.save(detalleOrden);
	}
	
}
