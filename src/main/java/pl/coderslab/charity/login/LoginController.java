package pl.coderslab.charity.login;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.security.UserServiceImpl;

@Controller
@AllArgsConstructor
public class LoginController {

    private UserServiceImpl userService;

    @GetMapping("/login")
    public String loginForm() {

        return "login";
    }

    @PostMapping("/login")
    public String login() {

        return "redirect:/";
    }
}
