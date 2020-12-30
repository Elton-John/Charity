package pl.coderslab.charity.admin;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.security.User;
import pl.coderslab.charity.security.UserServiceImpl;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminController {

    private UserServiceImpl userService;
    private AdminService adminService;


    @GetMapping("/admin")
    public String cockpit() {
        return "admin";
    }


    @GetMapping("/admin/all")
    public String adminList(Model model) {
        model.addAttribute("admins", adminService.getAll());
        return "admins";
    }


    @GetMapping("/admin/add")
    public String createAdmin(Model model) {
        model.addAttribute("newAdmin", new User());
        return "admin_register";
    }


    @PostMapping("/admin/add")
    public String saveNewAdmin(@ModelAttribute("newAdmin") @Valid User admin, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin_register";
        } else {
            User byUserName = userService.findByUserName(admin.getUsername());
            if (byUserName != null) {
                model.addAttribute("errorMessage", "użytkownik z taką nazwą już istnieje");
                return "admin_register";
            }
            adminService.saveAdmin(admin);
        }
        return "redirect:/admin/all";
    }


    @GetMapping("/admin/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {

        UserDto userDto = userService.getUserDtoOrThrow(id);
        model.addAttribute("admin", userDto);
        return "admin_edit";
    }


    @PostMapping("/admin/edit")
    public String editInstitution(@ModelAttribute("admin") @Valid UserDto admin, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin_edit";
        } else {
            User byUserName = userService.findByUserName(admin.getUsername());
            if (byUserName != null) {
                model.addAttribute("errorMessage", "użytkownik z taką nazwą już istnieje");
                return "admin_edit";
            }
            adminService.update(admin);
        }

        return "redirect:/admin/all";
    }


    @GetMapping("/admin/confirm/{id}")
    public String confirmDeleting(Model model, @PathVariable Long id) {
        model.addAttribute("admin", adminService.getOneOrThrow(id));
        return "admin_confirm";
    }


    @GetMapping("/admin/delete/{id}")
    public String deleteInstitution(@PathVariable Long id) {
        adminService.delete(id);
        return "redirect:/admin/all";
    }


}