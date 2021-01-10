package pl.coderslab.charity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "{username.notempty}")
    private String username;
    @NotEmpty(message = "{password.notempty}")
    @Pattern(regexp ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$", message = "{password.regex}")
    private String password;
    @NotEmpty(message = "{email.notempty}")
    @Email
    private String email;
}
