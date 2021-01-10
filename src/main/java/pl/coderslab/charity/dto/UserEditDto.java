package pl.coderslab.charity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class UserEditDto {
    private Long id;
    @NotEmpty(message = "{field.notempty}")
    private String name;
    @NotEmpty(message = "{field.notempty}")
    private String surname;
    @NotEmpty(message = "{email.notempty}")
    @Email
    private String email;
}
