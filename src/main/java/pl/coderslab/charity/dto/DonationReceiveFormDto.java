package pl.coderslab.charity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DonationReceiveFormDto {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receivedDate;
}
