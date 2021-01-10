package pl.coderslab.charity.admin;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    public List<User> getAll() {
        return userRepository.findAllAdmins();
    }


    public void saveAdmin(User admin) {
        admin.setEnabled(true);
        Role userRole = roleRepository.findByName("ROLE_ADMIN");
        admin.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(admin);
    }


    public void update(User user) {
        userRepository.save(user);
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
        Role userRole = roleRepository.findByName("ROLE_USER");
        admin.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(admin);
    }
}
