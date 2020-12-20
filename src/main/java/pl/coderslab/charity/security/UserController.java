package pl.coderslab.charity.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserService userService;
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

//    @GetMapping("/create-user")
//    @ResponseBody
//    public String createUser() {
//        User user = new User();
//        user.setUsername("admin");
//        user.setPassword("admin");
//        userService.saveUser(user);
//        return "admin";
//    }




    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("newUser", new User());
        return "register";
    }

    @PostMapping("/register")
    public String saveNewUser(@ModelAttribute("newUser") @Valid User user, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "register";
        }

        else {
            User byUserName = userService.findByUserName(user.getUsername());
            if(byUserName!=null){
                model.addAttribute("errorMessage", "użytkownik z taką nazwą już istnieje");
                return "login";
            }
            userService.saveUser(user);
        }
        return "redirect:/login";
    }


}

