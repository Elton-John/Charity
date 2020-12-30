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
public class UserManageController {

    private UserServiceImpl userService;
    private AdminService adminService;


    @GetMapping("/admin/user/all")
    public String userList(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "users";
    }


    @GetMapping("/admin/user/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {

        UserDto userDto = userService.getUserDtoOrThrow(id);
        model.addAttribute("user", userDto);
        return "user_edit";
    }


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


    @GetMapping("/admin/user/confirm/{id}")
    public String confirmDeleting(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.getOneOrThrow(id));
        return "user_confirm";
    }


    @GetMapping("/admin/user/delete/{id}")
    public String deleteInstitution(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/user/all";
    }
}
