package pl.coderslab.charity.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.dto.UserDto;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsernameAndArchivedIsFalse(username);
    }


    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
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
}