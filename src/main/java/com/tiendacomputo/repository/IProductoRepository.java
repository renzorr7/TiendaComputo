package com.tiendacomputo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tiendacomputo.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {
	

    @Query("SELECT p FROM Producto p WHERE p.categoria.id_categoria = :idCategoria")
    List<Producto> findProductosByCategoriaId(@Param("idCategoria") Integer idCategoria);

}