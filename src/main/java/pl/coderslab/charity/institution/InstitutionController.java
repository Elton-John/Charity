package pl.coderslab.charity.institution;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.model.Institution;

@Controller
@AllArgsConstructor
@Secured("ROLE_ADMIN")
public class InstitutionController {
    private InstitutionService institutionService;


    @GetMapping("admin/institutions")
    public String institutionList(Model model) {
        model.addAttribute("institutions", institutionService.getAllInstitutions());
        return "institutions";
    }


    @GetMapping("/admin/institutions/add")
    public String addInstitutionForm(Model model) {
        model.addAttribute("institution", new Institution());
        return "institution_add";
    }


    @PostMapping("/admin/institutions/add")
    public String addInstitution(@ModelAttribute Institution institution) {
        institutionService.create(institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("/admin/institutions/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {
        Institution institution = institutionService.getOneOrThrow(id);
        model.addAttribute("institution", institution);
        return "institution_edit";
    }


    @PostMapping("/admin/institutions/edit")
    public String editInstitution(@ModelAttribute Institution institution) {
        institutionService.save(institution);
        return "redirect:/admin/institutions";
    }


    @GetMapping("/admin/institutions/confirm/{id}")
    public String confirmDeleting(Model model, @PathVariable Long id) {
        model.addAttribute("institution", institutionService.getOneOrThrow(id));
        return "institution_confirm";
    }


    @GetMapping("/admin/institutions/delete/{id}")
    public String deleteInstitution(@PathVariable Long id) {
        institutionService.delete(id);
        return "redirect:/admin/institutions";
    }

}
