package com.ecommerce.service;

import java.util.Optional;

import com.ecommerce.model.Usuario;

public interface IUsuarioService {
	public Optional<Usuario> findById(Integer id);
	
}
