package pl.coderslab.charity.institution;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.model.CurrentUser;

@Controller
@AllArgsConstructor
@Secured("ROLE_ADMIN")
public class InstitutionController {
    private InstitutionService institutionService;


    @GetMapping("admin/institution/all")
    public String institutionList(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("admin", customUser.getUser());
        model.addAttribute("institutions", institutionService.getAllInstitutions());
        return "admin_panel/institutions/institution_all";
    }


    @GetMapping("/admin/institution/add")
    public String addInstitutionForm(Model model) {
        model.addAttribute("institution", new Institution());
        return "admin_panel/institutions/institution_add";
    }


    @PostMapping("/admin/institution/add")
    public String addInstitution(@ModelAttribute Institution institution) {
        institutionService.create(institution);
        return "redirect:/admin/institution/all";
    }

    @GetMapping("/admin/institution/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {
        Institution institution = institutionService.getOneOrThrow(id);
        model.addAttribute("institution", institution);
        return "admin_panel/institutions/institution_edit";
    }


    @PostMapping("/admin/institution/edit/{id}")
    public String editInstitution(@ModelAttribute Institution institution) {
        institutionService.save(institution);
        return "redirect:/admin/institution/all";
    }


    @GetMapping("/admin/institution/confirm/{id}")
    public String confirmDeleting(Model model, @PathVariable Long id) {
        model.addAttribute("institution", institutionService.getOneOrThrow(id));
        return "admin_panel/institutions/institution_confirm";
    }


    @GetMapping("/admin/institution/delete/{id}")
    public String deleteInstitution(@PathVariable Long id) {
        institutionService.delete(id);
        return "redirect:/admin/institution/all";
    }

}
