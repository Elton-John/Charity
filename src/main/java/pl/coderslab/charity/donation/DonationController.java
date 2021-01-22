package pl.coderslab.charity.donation;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.category.CategoryRepository;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.dto.DonationReceiveFormDto;
import pl.coderslab.charity.institution.InstitutionRepository;
import pl.coderslab.charity.model.Category;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.model.CurrentUser;

import java.util.List;

@Controller
@AllArgsConstructor
public class DonationController {
    private DonationService donationService;
    private CategoryRepository categoryRepository;
    private InstitutionRepository institutionRepository;


    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("donation", new DonationDto());
        model.addAttribute("categories");
        model.addAttribute("institutions");
        return "user_profile/form";
    }


    @PostMapping("/form/confirmation")
    public String confirmation(@AuthenticationPrincipal CurrentUser currentUser, @ModelAttribute DonationDto donationDto) {
        Donation donation = new Donation();
        donation.setCategories(donationDto.getCategories());
        donation.setCity(donationDto.getCity());
        donation.setInstitution(donationDto.getInstitution());
        donation.setPickUpComment(donationDto.getPickUpComment());
        donation.setPickUpDate(donationDto.getPickUpDate());
        donation.setPickUpTime(donationDto.getPickUpTime());
        donation.setQuantity(donationDto.getQuantity());
        donation.setStreet(donationDto.getStreet());
        donation.setZipCode(donationDto.getZipCode());
        donationService.create(donation, currentUser.getUser());
        return "redirect:/";
    }


    @GetMapping("/donation/{id}")
    public String donationInfo(@PathVariable Long id, Model model) {
        model.addAttribute("donation", donationService.getOneOrThrow(id));
        return "user_profile/donation_info";
    }


    @GetMapping("/donation/receive/{id}")
    public String receiveForm(@PathVariable Long id, Model model) {
        model.addAttribute("donation", donationService.getDonationReceiveFormDtoOrThrow(id));
        return "user_profile/donation_receive";
    }


    @PostMapping("/donation/receive")
    public String receive(@ModelAttribute("donation") DonationReceiveFormDto receiveFormDto) {
        donationService.receive(receiveFormDto);
        return "redirect:/donation/" + receiveFormDto.getId();
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
