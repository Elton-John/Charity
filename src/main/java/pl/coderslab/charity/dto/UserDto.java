package pl.coderslab.charity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotNull(message = "{password.notemty}")
    private String username;
    @NotNull(message = "{password.notemty}")
    private String password;
    @NotEmpty(message = "{email.notempty}")
    @Email
    private String email;
}
