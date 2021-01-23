package pl.coderslab.charity.donation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @InjectMocks
    private DonationService donationService;


    @DisplayName("Newly created donation has User set")
    @Test
    void newlyCreatedDonationShouldHaveUserSet() {
        //given
        Donation donation = new Donation();
        User user = new User();

        //when
        donationService.create(donation, user);

        //then
        verify(donationRepository).save(donation);
        assertThat(donation.getUser(), sameInstance(user));
    }

    @DisplayName("Newly created donation has Received set as False")
    @Test
    void newlyCreatedDonationShouldHaveReceivedAsFalseSet() {
        //given
        Donation donation = new Donation();
        User user = new User();

        //when
        donationService.create(donation, user);

        //then
        verify(donationRepository).save(donation);
        assertFalse(donation.isReceived());

    }
}