package pl.coderslab.charity.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.coderslab.charity.institution.InstitutionService;
import pl.coderslab.charity.security.UserServiceImpl;

@Controller
@AllArgsConstructor
public class AdminController {

    private UserServiceImpl userService;
    private InstitutionService institutionService;


    @GetMapping("/admin")
    public String cockpit() {
        return "admin";
    }


    @GetMapping("/admin/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }
}