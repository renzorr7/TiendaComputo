package com.tiendacomputo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendacomputo.model.RecepcionMercancia;

@Repository
public interface IRecepcionMercanciaRepository extends JpaRepository<RecepcionMercancia, Integer> {

}