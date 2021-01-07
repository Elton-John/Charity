package pl.coderslab.charity.user.resetPassword;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.email.EmailService;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ResetPasswordService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;


    public User getUser(String resetPasswordToken) {
        return resetPasswordTokenRepository.findByToken(resetPasswordToken).getUser();
    }


    public void createResetPasswordToken(User user, String token) {
        ResetPasswordToken myToken = new ResetPasswordToken(token, user);
        resetPasswordTokenRepository.save(myToken);
    }


    public void sendResetPasswordToken(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        createResetPasswordToken(user, token);
        String contextPath = request.getContextPath();
        String message = "Zrezetuj hasło" + "\r\n" + "http://localhost:8080" + contextPath + "/password/reset/new?token=" + token;
        emailService.send(email, "Password reset", message);
    }


    public boolean tokenExist(String token) {
        return resetPasswordTokenRepository.findByToken(token) != null;
    }


    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
