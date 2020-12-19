package pl.coderslab.charity.donation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.category.CategoryRepository;
import pl.coderslab.charity.institution.InstitutionRepository;
import pl.coderslab.charity.model.Category;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.Institution;

import java.util.List;

@Controller
@AllArgsConstructor
public class DonationController {
    private DonationRepository donationRepository;
    private CategoryRepository categoryRepository;
    private InstitutionRepository institutionRepository;

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("donation", new Donation());
        model.addAttribute("categories");
        model.addAttribute("institutions");
        return "form";
    }

    @PostMapping("/form/confirmation")
    public String confirmation( @ModelAttribute Donation donation) {
        donationRepository.save(donation);
        return "redirect:/";
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @ModelAttribute("institutions")
    public List<Institution> institutions() {
        return institutionRepository.findAll();
    }
}
