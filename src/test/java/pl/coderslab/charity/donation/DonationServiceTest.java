package pl.coderslab.charity.donation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DonationServiceTest {

    static DonationRepository donationRepository;
    static DonationService donationService;

    @BeforeAll
    static void setUp() {
        donationRepository = mock(DonationRepository.class);
        donationService = new DonationService(donationRepository);
    }

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