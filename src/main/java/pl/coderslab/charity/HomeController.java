package pl.coderslab.charity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.donation.DonationRepository;
import pl.coderslab.charity.institution.InstitutionRepository;
import pl.coderslab.charity.model.Institution;

import java.util.List;


@Controller
@AllArgsConstructor
public class HomeController {
    private InstitutionRepository institutionRepository;
    private DonationRepository donationRepository;


    @RequestMapping("/")
    public String homeAction(Model model) {
        model.addAttribute("institutions");
        model.addAttribute("quantity", donationRepository.countAllQuantity());
        return "index";
    }


    @ModelAttribute("institutions")
    public List<Institution> institutions() {
        return institutionRepository.findAll();
    }
}
