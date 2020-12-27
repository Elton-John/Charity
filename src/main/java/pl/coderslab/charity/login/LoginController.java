package pl.coderslab.charity.login;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class LoginController {


    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }


}
