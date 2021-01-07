package pl.coderslab.charity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.dto.UserDetailsDto;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.security.Role;
import pl.coderslab.charity.security.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService, IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private VerificationTokenRepository tokenRepository;


    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsernameAndArchivedIsFalse(username);
    }


    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setEnabled(true);
        user.setArchived(false);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }


    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }


    public List<User> allUsers() {
        return userRepository.findAllByArchivedFalse();
    }


    public UserDto getUserDtoOrThrow(Long id) {
        return userRepository.getUserDto(id).orElseThrow(EntityNotFoundException::new);
    }


    public void update(UserDto userDto) {
        User user = userRepository.getOne(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }


    public User getOneOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public void disable(Long id) {
        User user = userRepository.getOne(id);
        user.setEnabled(false);
        userRepository.save(user);
    }


    public void enable(Long id) {
        User user = userRepository.getOne(id);
        user.setEnabled(true);
        userRepository.save(user);
    }


    public void delete(Long id) {
        User user = userRepository.getOne(id);
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        user.setUsername(generatedString);
        user.setPassword(generatedString);
        user.setEnabled(false);
        user.setArchived(true);
        userRepository.save(user);
    }


    public UserDetailsDto getUserDetailsDto(String username) {
        return userRepository.getUserDetailDto(username);
    }


    public void updateDetail(UserDetailsDto userDetailsDto) {
        User user = userRepository.getOne(userDetailsDto.getId());
        user.setName(userDetailsDto.getName());
        user.setSurname(userDetailsDto.getSurname());
        user.setEmail(userDetailsDto.getEmail());
        userRepository.save(user);
    }


    public boolean changePassword(CurrentUser customUser, String oldPassword, String newPassword) {
        User user = customUser.getUser();
        boolean oldPasswordMatches = passwordEncoder.matches(oldPassword, user.getPassword());
        if (oldPasswordMatches) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    //////////////////////////////////////////////

    @Override
    public User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistException {

        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email adress: "
                            + userDto.getEmail());
        }

        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }


    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }


    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }


    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

}
