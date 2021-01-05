package pl.coderslab.charity.user;

import pl.coderslab.charity.dto.UserDto;

public interface IUserService {

    User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}