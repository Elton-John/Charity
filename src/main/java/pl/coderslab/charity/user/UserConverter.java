package pl.coderslab.charity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<String, User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User convert(String source) {
        return userRepository.getOne(Long.parseLong(source));
    }
}
