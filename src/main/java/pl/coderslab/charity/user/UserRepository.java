package pl.coderslab.charity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.dto.UserDetailsDto;
import pl.coderslab.charity.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndArchivedIsFalse(String username);

    List<User> findAllByArchivedFalse();

    @Query("SELECT u FROM User u JOIN FETCH u.roles r WHERE r.name = 'ROLE_ADMIN' ")
    List<User> findAllAdmins();

    @Query("SELECT new pl.coderslab.charity.dto.UserDto(u.id, u.username, u.password, u.email) FROM User u WHERE u.id = :id")
    Optional<UserDto> getUserDto(@Param("id") Long id);

    @Query("SELECT new pl.coderslab.charity.dto.UserDetailsDto(u.id, u.name, u.surname, u.email) FROM User u WHERE u.username = :username")
    UserDetailsDto getUserDetailDto(@Param ("username")String username);

    User getOneByUsername(String username);

    User findByEmail(String email);
}