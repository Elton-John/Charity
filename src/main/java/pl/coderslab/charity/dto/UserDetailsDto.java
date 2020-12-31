package pl.coderslab.charity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsDto {
    private Long id;
  //  private String username;
    private String name;
    private String surname;
    private String email;
}
