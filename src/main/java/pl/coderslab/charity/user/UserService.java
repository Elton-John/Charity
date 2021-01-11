package pl.coderslab.charity.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.donation.DonationService;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.dto.UserEditDto;
import pl.coderslab.charity.institution.InstitutionService;
import pl.coderslab.charity.model.CurrentUser;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.security.Role;
import pl.coderslab.charity.security.RoleRepository;

import javax.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DonationService donationService;
    private final InstitutionService institutionService;

    public User getOneOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User getByUserName(String username) {
        return userRepository.findByUsernameAndArchivedIsFalse(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> allUsers() {
        return userRepository.findAllByArchivedFalse();
    }


    public UserEditDto getUserEditDtoOrThrow(Long id) {
        return userRepository.getUserEditDto(id).orElseThrow(EntityNotFoundException::new);
    }


    public void update(User user) {
        userRepository.save(user);
    }


    public void blockUserByAdmin(Long id) {
        User user = userRepository.getOne(id);
        user.setEnabled(false);
        userRepository.save(user);
    }


    public void unblockUserByAdmin(Long id) {
        User user = userRepository.getOne(id);
        user.setEnabled(true);
        userRepository.save(user);
    }


    public void archiveUser(Long id) {
        User user = userRepository.getOne(id);
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        user.setUsername(generatedString);
        user.setPassword(generatedString);
        user.setName(generatedString);
        user.setSurname(generatedString);
        user.setEmail(generatedString);
        user.setEnabled(false);
        user.setArchived(true);
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


    public boolean userAlreadyExist(UserDto userDto) {
        boolean result = false;
        User byUserName = getByUserName(userDto.getUsername());
        User byEmail = getUserByEmail(userDto.getEmail());
        if (byUserName != null || byEmail != null) {
            result = true;
        }
        return result;
    }

    public boolean emailExist(String email) {
        User byEmail = userRepository.findByEmail(email);
        return byEmail != null;
    }

    public void setTestAdminAndUsers() {
        if (userRepository.findByEmail("nauka.dinosaurus@gmail.com") == null) {

            User user = new User();
            user.setUsername("admin");
            user.setName("Angela");
            user.setSurname("Merkel");
            user.setEmail("nauka@gmail.com");
            user.setPassword(passwordEncoder.encode("000"));
            user.setEnabled(true);
            user.setArchived(false);
            Role userRole = roleRepository.findByName("ROLE_USER");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole, adminRole)));
            userRepository.save(user);

            User user2 = new User();
            user2.setUsername("user");
            user2.setName("Angelina");
            user2.setSurname("Jolie");
            user2.setEmail("olikdiz@mail.ru");
            user2.setPassword(passwordEncoder.encode("000"));
            user2.setEnabled(true);
            user2.setArchived(false);
            user2.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            userRepository.save(user2);

            User user3 = new User();
            user3.setUsername("user2");
            user3.setName("Bill");
            user3.setSurname("Gates");
            user3.setEmail("user2@gmail.com");
            user3.setPassword(passwordEncoder.encode("000"));
            user3.setEnabled(true);
            user3.setArchived(false);
            user3.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            userRepository.save(user3);

            Donation donation = new Donation();
            donation.setQuantity(2);
            donation.setReceived(false);
            donation.setPickUpDate(LocalDate.of(2020, 1, 20));
            donation.setPickUpTime(LocalTime.of(20, 50));
            donation.setInstitution(institutionService.getOneOrThrow(1L));
            donation.setUser(user2);
            donationService.save(donation);

            Donation donation2 = new Donation();
            donation2.setQuantity(3);
            donation2.setReceived(true);
            donation2.setReceivedDate(LocalDate.of(2020, 1, 1));
            donation2.setPickUpDate(LocalDate.of(2020, 1, 1));
            donation2.setPickUpTime(LocalTime.of(10, 30));
            donation2.setInstitution(institutionService.getOneOrThrow(2L));
            donation2.setUser(user2);
            donationService.save(donation2);
        }

    }

}
