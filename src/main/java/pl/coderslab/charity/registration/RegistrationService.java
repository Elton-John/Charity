package pl.coderslab.charity.registration;

import antlr.TokenStreamException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.email.EmailService;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.security.Role;
import pl.coderslab.charity.security.RoleRepository;
import pl.coderslab.charity.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;


    public void saveUserBeforeEmailVerification(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);
        sendVerificationTokenByEmail(user);
    }


    private void sendVerificationTokenByEmail(User user) {
        VerificationToken token = createVerificationToken(user);
        String confirmationUrl = "/register/confirm?token=" + token.getToken();
        String message = "Cześć " + user.getUsername() + ", potwierdż swoją rejestrację klikając w poniższy link" + "\r\n" + "http://localhost:8080" + confirmationUrl;
        emailService.send(user.getEmail(), "Potwierdź rejestrację", message);
    }


    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken userToken = new VerificationToken(token, user, 24);
        tokenRepository.save(userToken);
        return userToken;
    }


    public VerificationToken getVerificationTokenOrThrow(String VerificationToken) throws TokenStreamException {
        VerificationToken token = tokenRepository.findByToken(VerificationToken);
        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenStreamException("token wygasł");
        }
        return token;
    }


    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }


    public void saveUserAfterVerification(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

}
