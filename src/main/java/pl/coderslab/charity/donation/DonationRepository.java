package pl.coderslab.charity.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.charity.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
}
