package pl.coderslab.charity.donation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.user.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;


    public void create(DonationDto donationDto, User user) {
        Donation donation = new Donation();
        donation.setCategories(donationDto.getCategories());
        donation.setCity(donationDto.getCity());
        donation.setInstitution(donationDto.getInstitution());
        donation.setPickUpComment(donationDto.getPickUpComment());
        donation.setPickUpDate(donationDto.getPickUpDate());
        donation.setPickUpTime(donationDto.getPickUpTime());
        donation.setQuantity(donationDto.getQuantity());
        donation.setStreet(donationDto.getStreet());
        donation.setZipCode(donationDto.getZipCode());
        donation.setUser(user);
        donation.setReceived(false);
        donationRepository.save(donation);
    }


    public List<Donation> getAllByUser(Long id) {
        List<Donation> donations = donationRepository.findAllByUserId(id);
        return donations.stream().sorted((o1, o2) -> Boolean.compare(o1.isReceived(), o2.isReceived())).collect(Collectors.toList());
    }


    public Donation getOneOrThrow(Long id) {
        return donationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
