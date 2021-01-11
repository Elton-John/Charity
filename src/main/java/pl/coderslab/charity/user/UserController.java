package pl.coderslab.charity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.donation.DonationService;
import pl.coderslab.charity.dto.UserEditDto;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private DonationService donationService;


    @Secured("ROLE_USER")
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("user", userService.getUserEditDtoOrThrow(customUser.getUser().getId()));
        model.addAttribute("userName", customUser.getUsername());
        return "profile";
    }


    @Secured("ROLE_USER")
    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute("user") UserEditDto userEditDto) {
        User user = userService.getOneOrThrow(userEditDto.getId());
        user.setName(userEditDto.getName());
        user.setSurname(userEditDto.getSurname());
        user.setEmail(userEditDto.getEmail());
        userService.update(user);
        return "redirect:/profile";
    }

    @Secured("ROLE_USER")
    @GetMapping("/profile/donation/all")
    public String donations(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("user", customUser.getUser());
        model.addAttribute("userName", customUser.getUsername());
        model.addAttribute("donations", donationService.getAllByUser(customUser.getUser().getId()));
        return "profile_donation_all";
    }

    @Secured("ROLE_USER")
    @GetMapping("/profile/password/edit")
    public String changePasswordForm(@AuthenticationPrincipal CurrentUser customUser) {
        if (customUser == null) {
            return "redirect:/";
        }
        return "password_change";
    }


    @Secured("ROLE_USER")
    @PostMapping("/profile/password/edit")
    public String changePassword(@AuthenticationPrincipal CurrentUser customUser,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        boolean changed = userService.changePassword(customUser, oldPassword, newPassword);
        if (changed) {
            SecurityContextHolder.clearContext();
            return "password_change_success";
        }
        model.addAttribute("errorMessage", "coś poszło nie tak");
        return "password_change";
    }

}
