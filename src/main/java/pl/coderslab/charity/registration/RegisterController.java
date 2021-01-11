package pl.coderslab.charity.registration;

import antlr.TokenStreamException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class RegisterController {

    private final RegistrationService registrationService;
    private final UserService userService;


    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration/register";
    }


    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Model model,
            HttpServletRequest request, Errors errors) {
        if (result.hasErrors()) {
            return "registration/register";
        } else {
            if (userService.userAlreadyExist(userDto)) {
                model.addAttribute("message", "użytkownik z taką nazwą lub email już istnieje");
                return "registration/register";
            }

            User user = new User();

            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            registrationService.saveUserBeforeEmailVerification(user);

        }

        return "registration/register_success";
    }


    @GetMapping("/register/confirm")
    public String confirmRegistration
            (@RequestParam("token") String token) {

        VerificationToken verificationToken;
        try {
            verificationToken = registrationService.getVerificationTokenOrThrow(token);
        } catch (TokenStreamException e) {
            return "registration/register_verification_fail";
        }

        User user = verificationToken.getUser();
        registrationService.saveUserAfterVerification(user);
        return "redirect:/login";
    }
}

