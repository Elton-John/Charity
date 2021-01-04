package pl.coderslab.charity.admin;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.security.Role;
import pl.coderslab.charity.security.RoleRepository;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAllAdmins();
    }


    public void saveAdmin(User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setEnabled(true);
        Role userRole = roleRepository.findByName("ROLE_ADMIN");
        admin.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(admin);
    }


    public void update(UserDto adminDto) {
        User admin = userRepository.getOne(adminDto.getId());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        userRepository.save(admin);
    }


    public User getOneOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public void revokeAdminRole(Long id) {
        User admin = getOneOrThrow(id);
        Set<Role> roles = admin.getRoles();
        roles.forEach(role -> {
            if (role.getName().equals("ROLE_ADMIN")) {
                roles.remove(role);
            }
        });
        userRepository.save(admin);
    }
}
