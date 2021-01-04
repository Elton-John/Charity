package pl.coderslab.charity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.admin.AdminService;
import pl.coderslab.charity.donation.DonationService;
import pl.coderslab.charity.dto.UserDetailsDto;
import pl.coderslab.charity.dto.UserDto;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
//@Secured("ROLE_ADMIN")
public class UserController {

    private UserServiceImpl userService;
    private DonationService donationService;
    private AdminService adminService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/all")
    public String userList(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "users";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {

        UserDto userDto = userService.getUserDtoOrThrow(id);
        model.addAttribute("user", userDto);
        return "user_edit";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/user/edit")
    public String editInstitution(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user_edit";
        } else {
            User byUserName = userService.findByUserName(userDto.getUsername());
            if (byUserName != null) {
                model.addAttribute("errorMessage", "użytkownik z taką nazwą już istnieje");
                return "user_edit";
            }
            userService.update(userDto);
        }

        return "redirect:/admin/user/all";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/ban/{id}")
    public String banUser(@PathVariable Long id) {
        User user = userService.getOneOrThrow(id);
        if (user.isEnabled()) {
            userService.disable(id);
        } else if (!user.isEnabled()) {
            userService.enable(id);
        }
        return "redirect:/admin/user/all";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/confirm/{id}")
    public String confirmDeleting(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.getOneOrThrow(id));
        return "user_confirm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/user/delete/{id}")
    public String deleteInstitution(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/user/all";
    }

    @Secured("ROLE_USER")
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("userName", customUser.getUsername());
        model.addAttribute("donations", donationService.getAllByUser(customUser.getUser().getId()));
        return "profile";
    }

    @Secured("ROLE_USER")
    @GetMapping("/profile/edit")
    public String editProfileForm(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("user", userService.getUserDetailsDto(customUser.getUsername()));
        return "profile_edit";
    }

    @Secured("ROLE_USER")
    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute UserDetailsDto userDetailsDto) {
        userService.updateDetail(userDetailsDto);
        return "redirect:/profile";
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
