package pl.coderslab.charity.user.resetPassword;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class ResetPasswordController {

    private UserServiceImpl userService;
    private final ResetPasswordService resetPasswordService;


    @GetMapping("/password/reset")
    public String resetPasswordEmailForm() {
        return "password_reset_email";
    }


    @PostMapping("/password/reset")
    public String resetPasswordEmail(@RequestParam("email") String email,
                                     HttpServletRequest request,
                                     Model model) {
        if (userService.emailExist(email)) {
            resetPasswordService.sendResetPasswordToken(email, request);
            return "password_reset_email_success";
        }
        model.addAttribute("message", "Nie istneje użytkownik z takim adresem email.");
        return "password_reset_email";

    }


    @GetMapping("/password/reset/new")
    public String resetPasswordNewForm(@RequestParam("token") String token, Model model) {
        if (resetPasswordService.tokenExist(token)) {
            model.addAttribute("token", token);
            return "password_reset_new";
        }
        return "redirect:/";
    }


    @PostMapping("/password/reset/new")
    public String resetPasswordNew(@RequestParam("token") String token,
                                   @RequestParam("newPassword") String newPassword,
                                   @RequestParam("confirmPassword") String confirmPassword,
                                   Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "Hasła są różne");
            return "password_reset_new";
        }

        User user = resetPasswordService.getUser(token);
        resetPasswordService.resetPassword(user, newPassword);
        return "redirect:/login";
    }
}
