package com.tiendacomputo.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendacomputo.model.Categoria;
import com.tiendacomputo.model.Producto;
import com.tiendacomputo.repository.ICategoriaRepository;
import com.tiendacomputo.repository.IProductoRepository;



@Controller
@RequestMapping("/tienda/productos")
public class ProductoController {

    @Autowired
    private IProductoRepository productoRepo;

    @Autowired
    private ICategoriaRepository categoriaRepo;

    // Mostrar productos, con opción de filtrar por categoría y ordenar por precio
    @GetMapping
    public String listarProductos(
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) String ordenarPorPrecio,
            Model model) {

        List<Producto> productos;

        if (idCategoria != null) {
            productos = productoRepo.findProductosByCategoriaId(idCategoria);
        } else {
            productos = productoRepo.findAll();
        }

        // Si el usuario quiere ordenar por precio de menor a mayor, ordenamos la lista aquí
        if ("menorAMayor".equals(ordenarPorPrecio)) {
        	productos.sort(Comparator.comparing(Producto::getPrecio));        
        }

        model.addAttribute("listaProductos", productos);
        model.addAttribute("listaCategorias", categoriaRepo.findAll());
        model.addAttribute("selectedCategoryId", idCategoria);

        if (!model.containsAttribute("producto")) {
            model.addAttribute("producto", new Producto());
        }
        return "productos"; 
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirect) {
        try {
          
            productoRepo.save(producto);
            redirect.addFlashAttribute("mensaje", "Producto guardado exitosamente!");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error al guardar el producto: " + e.getMessage());
            redirect.addFlashAttribute("claseMensaje", "danger");
          
        }
        return "redirect:/tienda/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Producto producto = productoRepo.findById(id).orElse(null);

        if (producto == null) {
            redirect.addFlashAttribute("mensaje", "Producto no encontrado.");
            redirect.addFlashAttribute("claseMensaje", "danger");
            return "redirect:/tienda/productos";
        }

        model.addAttribute("producto", producto);
        model.addAttribute("listaProductos", productoRepo.findAll());
        model.addAttribute("listaCategorias", categoriaRepo.findAll());
        return "productos"; 
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            productoRepo.deleteById(id);
            redirect.addFlashAttribute("mensaje", "Producto eliminado exitosamente!");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error al eliminar el producto: " + e.getMessage());
            redirect.addFlashAttribute("claseMensaje", "danger");
        }
        return "redirect:/tienda/productos";
    }

    @GetMapping("/ver/{id}")
    public String verDetallesProducto(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Producto producto = productoRepo.findById(id).orElse(null);

        if (producto == null) {
            redirect.addFlashAttribute("mensaje", "Producto no encontrado.");
            redirect.addFlashAttribute("claseMensaje", "danger");
            return "redirect:/tienda/productos";
        }

        model.addAttribute("producto", producto);
        return "detalleProducto";
    }
}