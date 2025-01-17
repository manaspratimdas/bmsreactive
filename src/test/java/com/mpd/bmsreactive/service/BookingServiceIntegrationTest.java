package com.mpd.bmsreactive.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mpd.bmsreactive.domain.Booking;
import com.mpd.bmsreactive.repository.BookingRepository;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
public class BookingServiceIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        bookingRepository.deleteAll();
    }

    @Test
    public void testCreateBooking() {
        Booking booking = new Booking();
        booking.setReservation("John Doe");
        booking.setAvailability("Available");
        booking.setCancellation("None");
        booking.setCheckIn(LocalDate.of(2025, 1, 20));
        booking.setCheckOut(LocalDate.of(2025, 1, 25));

        webTestClient.post().uri("/bookings")
                .body(Mono.just(booking), Booking.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Booking.class)
                .value(response -> {
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getReservation()).isEqualTo("John Doe");
                });
    }

    @Test
    public void testGetAllBookings() {
        Booking booking1 = new Booking();
        booking1.setReservation("John Doe");
        booking1.setAvailability("Available");
        booking1.setCancellation("None");
        booking1.setCheckIn(LocalDate.of(2025, 1, 20));
        booking1.setCheckOut(LocalDate.of(2025, 1, 25));

        Booking booking2 = new Booking();
        booking2.setReservation("Jane Doe");
        booking2.setAvailability("Unavailable");
        booking2.setCancellation("None");
        booking2.setCheckIn(LocalDate.of(2025, 2, 1));
        booking2.setCheckOut(LocalDate.of(2025, 2, 5));

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        webTestClient.get().uri("/bookings")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Booking.class)
                .value(bookings -> {
                    assertThat(bookings).hasSize(2);
                    assertThat(bookings).extracting(Booking::getReservation).contains("John Doe", "Jane Doe");
                });
    }

    @Test
    public void testCheckRoomAvailability() {
        Booking booking = new Booking();
        booking.setReservation("John Doe");
        booking.setAvailability("Available");
        booking.setCancellation("None");
        booking.setCheckIn(LocalDate.of(2025, 1, 20));
        booking.setCheckOut(LocalDate.of(2025, 1, 25));

        bookingRepository.save(booking);

        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/bookings/availability")
                .queryParam("checkIn", "2025-01-15")
                .queryParam("checkOut", "2025-01-18")
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .value(response -> assertThat(response).isTrue());
    }

    @Test
    public void testCancelBooking() {
        Booking booking = new Booking();
        booking.setReservation("John Doe");
        booking.setAvailability("Available");
        booking.setCancellation("None");
        booking.setCheckIn(LocalDate.of(2025, 1, 20));
        booking.setCheckOut(LocalDate.of(2025, 1, 25));

        booking = bookingRepository.save(booking);

        webTestClient.post().uri("/bookings/cancel/{id}", booking.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Booking.class)
                .value(response -> assertThat(response.getCancellation()).isEqualTo("Cancelled"));
    }

    @Test
    public void testUpdateBooking() {
        Booking booking = new Booking();
        booking.setReservation("John Doe");
        booking.setAvailability("Available");
        booking.setCancellation("None");
        booking.setCheckIn(LocalDate.of(2025, 1, 20));
        booking.setCheckOut(LocalDate.of(2025, 1, 25));

        booking = bookingRepository.save(booking);

        Booking updatedBooking = new Booking();
        updatedBooking.setReservation("Jane Doe");
        updatedBooking.setAvailability("Unavailable");
        updatedBooking.setCancellation("None");
        updatedBooking.setCheckIn(LocalDate.of(2025, 2, 1));
        updatedBooking.setCheckOut(LocalDate.of(2025, 2, 5));

        webTestClient.put().uri("/bookings/{id}", booking.getId())
                .body(Mono.just(updatedBooking), Booking.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Booking.class)
                .value(response -> assertThat(response.getReservation()).isEqualTo("Jane Doe"));
    }
}
