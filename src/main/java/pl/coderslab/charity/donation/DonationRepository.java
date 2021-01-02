package pl.coderslab.charity.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.model.Donation;

import java.util.List;
import java.util.Set;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {


    @Query("SELECT SUM (d.quantity) FROM Donation d ")
    Integer countAllQuantity();


    @Query("SELECT d FROM Donation d WHERE d.user.id = :id")
    List<Donation> findAllByUserId(@Param("id") Long id);
}
