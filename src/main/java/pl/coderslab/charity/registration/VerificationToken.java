package pl.coderslab.charity.registration;

import lombok.*;
import pl.coderslab.charity.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity
@Setter
@Getter

@NoArgsConstructor

public class VerificationToken {
//    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public VerificationToken(String token, User user, int hours) {
        this.token = token;
        this.user = user;
        expiryDate = calculateExpiryDate(hours);
    }

//    private Date calculateExpiryDate(int expiryTimeInMinutes) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Timestamp(cal.getTime().getTime()));
//        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//        return new Date(cal.getTime().getTime());
//    }

    private LocalDateTime calculateExpiryDate (int expiryTimeInHours){
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(expiryTimeInHours);
    }

}