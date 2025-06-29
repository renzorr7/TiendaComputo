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

import com.tiendacomputo.model.Categoria;
import com.tiendacomputo.repository.ICategoriaRepository;

@Controller
@RequestMapping("/tienda/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaRepository categoriaRepo;

    @GetMapping
    public String mostrarCategorias(Model model) {
        model.addAttribute("listaCategorias", categoriaRepo.findAll());

        if (!model.containsAttribute("categoria")) {
            model.addAttribute("categoria", new Categoria());
        }
        return "categorias"; 
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria, RedirectAttributes redirect) {
        try {
            categoriaRepo.save(categoria);
            redirect.addFlashAttribute("mensaje", "Categoria guardada");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "Error al guardar: " + e.getMessage());
            redirect.addFlashAttribute("claseMensaje", "danger");
            redirect.addFlashAttribute("categoria", categoria);
        }
        return "redirect:/tienda/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Categoria categoria = categoriaRepo.findById(id).orElse(null);

        if (categoria == null) {
            redirect.addFlashAttribute("mensaje", "Categoria no encontrada.");
            redirect.addFlashAttribute("claseMensaje", "danger");
            return "redirect:/tienda/categorias";
        }

        model.addAttribute("categoria", categoria);
        model.addAttribute("listaCategorias", categoriaRepo.findAll());
        return "categorias"; 
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            categoriaRepo.deleteById(id);
            redirect.addFlashAttribute("mensaje", "Categoria eliminada");
            redirect.addFlashAttribute("claseMensaje", "success");
        } catch (Exception e) {
            redirect.addFlashAttribute("mensaje", "No se pudo eliminar");
            redirect.addFlashAttribute("claseMensaje", "danger");
        }
        return "redirect:/tienda/categorias";
    }
}