package pl.coderslab.charity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.donation.DonationRepository;
import pl.coderslab.charity.email.EmailService;
import pl.coderslab.charity.institution.InstitutionRepository;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.user.UserService;

import java.util.List;


@Controller
@AllArgsConstructor
public class HomeController {
    private InstitutionRepository institutionRepository;
    private DonationRepository donationRepository;
    private EmailService emailService;
    private UserService userService;


    @RequestMapping("/")
    public String homeAction(Model model) {
        userService.setTestAdminAndUsers();
        model.addAttribute("institutions");
        model.addAttribute("quantity", donationRepository.countAllQuantity());
        model.addAttribute("total", donationRepository.count());
        return "index";
    }


    @ModelAttribute("institutions")
    public List<Institution> institutions() {
        return institutionRepository.findAllByArchiveIsFalse();
    }

    @GetMapping("/email")
    public String send() {
        emailService.sendTestEmail();
        return "redirect:/";
    }
}
