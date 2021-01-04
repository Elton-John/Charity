package pl.coderslab.charity.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    private String getSecretPassword() {
        String secretPassword;
        try {
            Path path = Paths.get("src/pass.txt");
            List<String> strings = Files.readAllLines(path);
            Optional<String> first = strings.stream().filter(s -> s.matches("[a-z0-9]*")).findFirst();
            if (first.isPresent()) {
                secretPassword = first.get();
                return secretPassword;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }


    @Value("465")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;
    @Bean
    public JavaMailSender getMailSender(){
        String secretPassword = getSecretPassword();
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(secretPassword);
        javaMailSender.setPort(port);

        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.setProperty("mail.transport.protocol", protocol);
        javaMailProperties.setProperty("mail.debug", debug);

        return javaMailSender;
    }

}