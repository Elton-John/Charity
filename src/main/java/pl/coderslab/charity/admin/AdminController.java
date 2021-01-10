package pl.coderslab.charity.admin;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.dto.UserEditDto;
import pl.coderslab.charity.user.CurrentUser;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/admin")
    public String cockpit(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("admin", customUser.getUser());
        return "admin_panel/panel";
    }


    @GetMapping("/admin/all")
    public String adminList(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("admin", customUser.getUser());
        model.addAttribute("admins", adminService.getAll());
        return "admin_panel/admins/admins";
    }


    @GetMapping("/admin/add")
    public String createAdmin(Model model) {
        model.addAttribute("newAdmin", new UserDto());
        return "admin_panel/admins/admin_add";
    }


    @PostMapping("/admin/add")
    public String saveNewAdmin(@ModelAttribute("newAdmin") @Valid UserDto userDto,
                               BindingResult result,
                               @RequestParam("password2") String password2,
                               Model model) {
        if (result.hasErrors()) {
            return "admin_panel/admins/admin_add";
        } else {
            User byUserName = userService.getByUserName(userDto.getUsername());
            User byEmail = userService.getUserByEmail(userDto.getEmail());
            if (byUserName != null || byEmail != null) {
                model.addAttribute("message", "użytkownik z taką nazwą lub email już istnieje");
                return "admin_panel/admins/admin_add";
            } else if (!userDto.getPassword().equals(password2)) {
                model.addAttribute("message", "hasła są róźne");
                return "admin_panel/admins/admin_add";
            }
            User admin = new User();
            admin.setUsername(userDto.getUsername());
            admin.setEmail(userDto.getEmail());
            admin.setPassword(passwordEncoder.encode(userDto.getPassword()));
            adminService.saveAdmin(admin);
        }
        return "redirect:/admin/all";
    }


    @GetMapping("/admin/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {

        UserEditDto userEditDto = userService.getUserEditDtoOrThrow(id);
        model.addAttribute("admin", userEditDto);
        return "admin_panel/admins/admin_edit";
    }


    @PostMapping("/admin/edit/{id}")
    public String editInstitution(@ModelAttribute("admin") @Valid UserEditDto userEditDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin_panel/admins/admin_edit";
        }

        User user = userService.getOneOrThrow(userEditDto.getId());
        user.setName(userEditDto.getName());
        user.setSurname(userEditDto.getSurname());
        user.setEmail(userEditDto.getEmail());
        adminService.update(user);

        return "redirect:/admin/all";
    }


    @GetMapping("/admin/confirm/{id}")
    public String confirmDeleting(Model model, @PathVariable Long id) {
        model.addAttribute("admin", adminService.getOneOrThrow(id));
        return "admin_panel/admins/admin_revoke_confirm";
    }


    @GetMapping("/admin/delete/{id}")
    public String deleteInstitution(@AuthenticationPrincipal CurrentUser customUser,
                                    @PathVariable Long id,
                                    Model model) {
        if (customUser.getUser().getId().equals(id)) {
            model.addAttribute("admin", adminService.getOneOrThrow(id));
            model.addAttribute("message", "Próbujesz usunąć samego siebie. To nie jest możliwe");
            return "admin_panel/admins/admin_revoke_confirm";
        }
        adminService.revokeAdminRole(id);
        return "redirect:/admin/all";
    }
}