package pl.coderslab.charity.donation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.dto.DonationDto;
import pl.coderslab.charity.dto.DonationReceiveFormDto;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.user.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        return donations.stream().sorted(Comparator.comparing(Donation::isReceived)
                .thenComparing(Donation::getReceivedDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(Donation::getCreatedOn, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }


    public Donation getOneOrThrow(Long id) {
        return donationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public DonationReceiveFormDto getDonationReceiveFormDtoOrThrow(Long id) {
        return donationRepository.getDonationReceiveFormDto(id).orElseThrow(EntityNotFoundException::new);
    }


    public void receive(DonationReceiveFormDto dto) {
        Donation donation = donationRepository.getOne(dto.getId());
        donation.setReceivedDate(dto.getReceivedDate());
        donation.setReceived(true);
        donation.setUpdatedOn(LocalDateTime.now());
        donationRepository.save(donation);
    }
}
