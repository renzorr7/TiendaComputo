package com.tiendacomputo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendacomputo.model.DetalleRecepcion;

@Repository
public interface IDetalleRecepcionRepository extends JpaRepository<DetalleRecepcion, Integer> {
	
}

