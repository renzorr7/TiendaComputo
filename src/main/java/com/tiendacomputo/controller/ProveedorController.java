package com.tiendacomputo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendacomputo.model.Proveedor;
import com.tiendacomputo.repository.IProveedorRepository;
 


@Controller
@RequestMapping("/tienda/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorRepository proveedorRepo;

    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("listaProveedores", proveedorRepo.findAll());

        if (!model.containsAttribute("proveedor")) {
            model.addAttribute("proveedor", new Proveedor());
        }
        return "proveedores"; 
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes redirect) {
        try {
            proveedorRepo.save(proveedor);
            redirect.addFlashAttribute("mensaje", "Proveedor guardado exitosamente!");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error al guardar el proveedor: " + e.getMessage());
            redirect.addFlashAttribute("claseMensaje", "danger");
            redirect.addFlashAttribute("proveedor", proveedor);
        }
        return "redirect:/tienda/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Proveedor proveedor = proveedorRepo.findById(id).orElse(null);

        if (proveedor == null) {
            redirect.addFlashAttribute("mensaje", "Proveedor no encontrado");
            redirect.addFlashAttribute("claseMensaje", "danger");
            return "redirect:/tienda/proveedores";
        }

        model.addAttribute("proveedor", proveedor);
        model.addAttribute("listaProveedores", proveedorRepo.findAll());
        return "proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            proveedorRepo.deleteById(id);
            redirect.addFlashAttribute("mensaje", "Proveedor eliminado exitosamente");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "No se pudo eliminar");
            redirect.addFlashAttribute("claseMensaje", "danger");
        }
        return "redirect:/tienda/proveedores";
    }
}
