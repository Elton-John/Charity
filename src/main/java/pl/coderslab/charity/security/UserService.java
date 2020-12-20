package pl.coderslab.charity.security;

public interface UserService {
    User findByUserName(String name);

    void saveUser(User user);
}