package pl.coderslab.charity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.security.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter

@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    private String username;
    private String name;
    private String surname;
    private String password;
    private String email;
    private boolean enabled;
    private boolean archived;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role"
            , joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private List<Donation> donations;

    public User() {
        super();
        this.enabled=false;
    }
}
