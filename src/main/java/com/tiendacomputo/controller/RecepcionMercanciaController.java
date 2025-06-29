package com.tiendacomputo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendacomputo.model.DetalleRecepcion;
import com.tiendacomputo.model.Producto;
import com.tiendacomputo.model.Proveedor;
import com.tiendacomputo.model.RecepcionMercancia;
import com.tiendacomputo.repository.*;

import jakarta.transaction.Transactional;
import jakarta.servlet.http.HttpServletRequest;
 


@Controller
@RequestMapping("/tienda/recepcion")
public class RecepcionMercanciaController {

    @Autowired
    private IRecepcionMercanciaRepository recepcionRepo;

    @Autowired
    private IDetalleRecepcionRepository detalleRepo;

    @Autowired
    private IProductoRepository productoRepo;

    @Autowired
    private IProveedorRepository proveedorRepo;

    @GetMapping
    public String mostrarFormularioRecepcion(Model model) {
        model.addAttribute("recepcion", new RecepcionMercancia());  
        model.addAttribute("listaProveedores", proveedorRepo.findAll()); 
        model.addAttribute("listaProductos", productoRepo.findAll());   
        return "recepcionMercancia";  
    }

    @PostMapping("/guardar")
    @Transactional
    public String guardarRecepcion(
            @ModelAttribute RecepcionMercancia recepcion,
            HttpServletRequest request,
            RedirectAttributes redirect) {

        try {
            // Buscar el proveedor seleccionado
            Integer idProveedor = recepcion.getProveedor().getId_proveedor();
            Proveedor proveedor = proveedorRepo.findById(idProveedor)
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con ID: " + idProveedor));

            recepcion.setProveedor(proveedor);

            recepcion.setFecha_recepcion(LocalDateTime.now());

            RecepcionMercancia recepcionGuardada = recepcionRepo.save(recepcion);


            // Recorremos un máximo de 100 productos enviados en el formulario
            for (int i = 1; i <= 100; i++) {
                String idProductoStr = request.getParameter("productoId_" + i);
                String cantidadStr = request.getParameter("cantidad_" + i);

                // Si se seleccionó un producto en esa posición del formulario
                if (idProductoStr != null && !idProductoStr.isEmpty()) {
                    Integer idProducto = Integer.parseInt(idProductoStr);

                    Integer cantidad = Integer.parseInt(cantidadStr);

                   
                    Producto producto = productoRepo.findById(idProducto)
                                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + idProducto));

                    DetalleRecepcion detalle = new DetalleRecepcion();
                    detalle.setRecepcion_mercancia(recepcionGuardada);
                    detalle.setProducto(producto);
                    detalle.setCantidad(cantidad);
                    detalleRepo.save(detalle);

                    // Actualizar el stock del producto
                    producto.setStock(producto.getStock() + cantidad);
                    productoRepo.save(producto);
                }
            }

          

            redirect.addFlashAttribute("mensaje", "Recepción registrada y stock actualizado");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error al registrar recepción: " + e.getMessage());
            redirect.addFlashAttribute("claseMensaje", "danger");
        }

        return "redirect:/tienda/productos";
    }
}
