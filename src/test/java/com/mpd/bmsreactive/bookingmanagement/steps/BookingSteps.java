package com.mpd.bmsreactive.bookingmanagement.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mpd.bmsreactive.domain.Booking;
import com.mpd.bmsreactive.repository.BookingRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingSteps extends BmsreactiveApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking;
    private WebTestClient.ResponseSpec response;

    @Given("the booking details")
    public void theBookingDetails() {
        booking = new Booking();
        booking.setReservation("John Doe");
        booking.setAvailability("Available");
        booking.setCancellation("None");
        booking.setCheckIn(LocalDate.of(2025, 1, 20));
        booking.setCheckOut(LocalDate.of(2025, 1, 25));
    }

    @When("the client sends a POST request to {string}")
    public void theClientSendsAPostRequestTo(String uri) {
        response = webTestClient.post().uri(uri)
                .body(Mono.just(booking), Booking.class)
                .exchange();
    }

    @Then("the booking is created successfully")
    public void theBookingIsCreatedSuccessfully() {
        response.expectStatus().isOk()
                .expectBody(Booking.class)
                .value(responseBooking -> {
                    assertThat(responseBooking.getId()).isNotNull();
                    assertThat(responseBooking.getReservation()).isEqualTo("John Doe");
                });
    }

    @Given("there are bookings in the system")
    public void thereAreBookingsInTheSystem() {
        bookingRepository.deleteAll();
        bookingRepository.save(
                new Booking(null, "John Doe", "Available", "None", LocalDate.of(2025, 1, 20),
                        LocalDate.of(2025, 1, 25)));
        bookingRepository.save(
                new Booking(null, "Jane Doe", "Unavailable", "None", LocalDate.of(2025, 2, 1),
                        LocalDate.of(2025, 2, 5)));
    }

    @When("the client sends a GET request to \"/bookings\"")
    public void theClientSendsAGetRequestToBookings() {
        response = webTestClient.get().uri("/bookings").exchange();
    }

    @Then("the client receives a list of bookings")
    public void theClientReceivesAListOfBookings() {
        response.expectStatus().isOk()
                .expectBodyList(Booking.class)
                .value(bookings -> {
                    assertThat(bookings).hasSize(2);
                    assertThat(bookings).extracting(Booking::getReservation).contains("John Doe", "Jane Doe");
                });
    }

    @When("the client sends a GET request to \"/bookings/availability\" with check-in and check-out dates")
    public void theClientSendsAGetRequestToBookingsAvailabilityWithCheckInAndCheckOutDates() {
        response = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/bookings/availability")
                .queryParam("checkIn", "2025-01-15")
                .queryParam("checkOut", "2025-01-18")
                .build()).exchange();
    }

    @Then("the client receives the room availability status")
    public void theClientReceivesTheRoomAvailabilityStatus() {
        response.expectStatus().isOk()
                .expectBody(Boolean.class)
                .value(availability -> assertThat(availability).isTrue());
    }

    @Given("there is a booking in the system")
    public void thereIsABookingInTheSystem() {
        bookingRepository.deleteAll();
        booking = bookingRepository.save(
                new Booking(1L, "John Doe", "Available", "None", LocalDate.of(2025, 1, 20),
                        LocalDate.of(2025, 1, 25)));
    }

    @When("the client sends a POST request to \"/bookings/cancel/1\"")
    public void theClientSendsAPostRequestToBookingsCancel(int id) {
        response = webTestClient.post().uri("/bookings/cancel/" + id)
                .exchange();
    }

    @Then("the booking is cancelled successfully")
    public void theBookingIsCancelledSuccessfully() {
        response.expectStatus().isOk()
                .expectBody(Booking.class)
                .value(responseBooking -> assertThat(responseBooking.getCancellation()).isEqualTo("Cancelled"));
    }

    @When("the client sends a PUT request to \"/bookings/1\" with updated booking details")
    public void theClientSendsAPutRequestToBookingsIdWithUpdatedBookingDetails(int id) {
        Booking updatedBooking = new Booking(null, "Jane Doe", "Unavailable", "None",
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 2, 5));
        response = webTestClient.put().uri("/bookings/" + id)
                .body(Mono.just(updatedBooking), Booking.class)
                .exchange();
    }

    @Then("the booking is updated successfully")
    public void theBookingIsUpdatedSuccessfully() {
        response.expectStatus().isOk()
                .expectBody(Booking.class)
                .value(responseBooking -> assertThat(responseBooking.getReservation()).isEqualTo("Jane Doe"));
    }

}
