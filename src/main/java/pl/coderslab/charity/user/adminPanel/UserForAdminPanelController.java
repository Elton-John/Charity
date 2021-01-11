package pl.coderslab.charity.user.adminPanel;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.dto.UserEditDto;
import pl.coderslab.charity.user.CurrentUser;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Secured("ROLE_ADMIN")
public class UserForAdminPanelController {

    private UserService userService;



    @GetMapping("/admin/user/all")
    public String userList(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        model.addAttribute("admin", customUser.getUser());
        model.addAttribute("users", userService.allUsers());
        return "admin_panel/users/user_all";
    }


    @GetMapping("/admin/user/edit/{id}")
    public String editInstitutionForm(Model model, @PathVariable Long id) {
        UserEditDto userEditDto = userService.getUserEditDtoOrThrow(id);
        model.addAttribute("user", userEditDto);
        return "admin_panel/users/user_edit";
    }


    @PostMapping("/admin/user/edit/{id}")
    public String editInstitution(@ModelAttribute("user") @Valid UserEditDto userEditDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin_panel/users/user_edit";
        }
        User byUserName = userService.getUserByEmail(userEditDto.getEmail());
        if (byUserName != null && !byUserName.getId().equals(userEditDto.getId())) {
            model.addAttribute("errorMessage", "użytkownik z taką nazwą już istnieje");
            return "admin_panel/users/user_edit";
        }
        User user = userService.getOneOrThrow(userEditDto.getId());
        user.setName(userEditDto.getName());
        user.setSurname(userEditDto.getSurname());
        user.setEmail(userEditDto.getEmail());
        userService.update(user);


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
        return "admin_panel/users/user_confirm";
    }


    @GetMapping("/admin/user/delete/{id}")
    public String deleteInstitution(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/user/all";
    }
}
