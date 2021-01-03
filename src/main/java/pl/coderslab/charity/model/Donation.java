package pl.coderslab.charity.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.coderslab.charity.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Setter
@Getter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;// (liczba worków)
    @ManyToMany
    @JoinTable(name = "donations_categories",
            joinColumns = @JoinColumn(name = "donation_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    @ManyToOne
    private Institution institution;
    private String street;
    private String city;
    private String zipCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;
    private LocalTime pickUpTime;
    private String pickUpComment;
    @ManyToOne()
    private User user;
    private boolean received;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receivedDate;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }
}
