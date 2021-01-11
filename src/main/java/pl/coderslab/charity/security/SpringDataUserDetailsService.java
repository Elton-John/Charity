package pl.coderslab.charity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.coderslab.charity.user.CurrentUser;
import pl.coderslab.charity.user.User;
import pl.coderslab.charity.user.UserService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpringDataUserDetailsService implements UserDetailsService {
    private UserService userService;
//    private Map<String, User> roles = new HashMap<>();


    @Autowired
    public void setUserRepository(UserService userService) {
        this.userService = userService;
    }


//    @PostConstruct
//    public void init() {
//        roles.put("admin2", new User("admin", "{noop}admin1", getAuthority("ROLE_ADMIN")));
//        roles.put("user2", new User("user", "{noop}user1", getAuthority("ROLE_USER")));
//    }

    private List<GrantedAuthority> getAuthority(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }


    @Override
    public CurrentUser loadUserByUsername(String username) {
        User user = userService.getByUserName(username);


        if (user == null) {
            throw new UsernameNotFoundException(username);

        }
        if (!user.isEnabled()) {
            throw new DisabledException("Konto zostało zablokowane przez administratora");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().forEach(r ->
                grantedAuthorities.add(new SimpleGrantedAuthority(r.getName())));
        return new CurrentUser(user.getUsername(),
                user.getPassword(),

                grantedAuthorities,
                user);
    }

}