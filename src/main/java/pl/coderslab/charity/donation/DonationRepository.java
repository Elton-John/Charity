package pl.coderslab.charity.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.dto.DonationReceiveFormDto;
import pl.coderslab.charity.model.Donation;

import java.util.List;
import java.util.Optional;


public interface DonationRepository extends JpaRepository<Donation, Long> {


    @Query("SELECT SUM (d.quantity) FROM Donation d ")
    Integer countAllQuantity();


  //  @Query("SELECT d FROM Donation d WHERE d.user.id = :id")
    List<Donation> findAllByUserId(@Param("id") Long id);


    @Query("SELECT new pl.coderslab.charity.dto.DonationReceiveFormDto(d.id, d.receivedDate) FROM Donation d WHERE d.id = :id")
    Optional<DonationReceiveFormDto> getDonationReceiveFormDto(@Param("id") Long id);
}
