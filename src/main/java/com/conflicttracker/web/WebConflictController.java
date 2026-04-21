package com.conflicttracker.web;

import com.conflicttracker.dto.ConflictDTO;
import com.conflicttracker.service.ConflictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/web/conflicts")
@RequiredArgsConstructor
public class WebConflictController {

    private final ConflictService conflictService;

    /**
     * Muestra el listado de todos los conflictos
     */
    @GetMapping
    public String listConflicts(Model model) {
        List<ConflictDTO> conflicts = conflictService.getAllConflictsDTO();
        model.addAttribute("conflicts", conflicts);
        model.addAttribute("totalConflicts", conflicts.size());
        return "conflicts/list";
    }

    /**
     * Muestra el formulario de creación de un nuevo conflicto
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("conflictDTO", new ConflictDTO());
        return "conflicts/create";
    }

    /**
     * Procesa la creación de un nuevo conflicto
     */
    @PostMapping
    public String createConflict(
            @Valid @ModelAttribute ConflictDTO conflictDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("conflictDTO", conflictDTO);
            return "conflicts/create";
        }

        try {
            ConflictDTO createdConflict = conflictService.createConflict(conflictDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Conflicto '" + createdConflict.getName() + "' creado exitosamente");
            return "redirect:/web/conflicts";
        } catch (Exception e) {
            model.addAttribute("conflictDTO", conflictDTO);
            model.addAttribute("error", "Error al crear el conflicto: " + e.getMessage());
            return "conflicts/create";
        }
    }

    /**
     * Muestra los detalles de un conflicto específico
     */
    @GetMapping("/{id}")
    public String showConflictDetails(@PathVariable Long id, Model model) {
        try {
            ConflictDTO conflict = conflictService.getConflictById(id);
            model.addAttribute("conflict", conflict);
            return "conflicts/details";
        } catch (Exception e) {
            return "redirect:/web/conflicts";
        }
    }
}
