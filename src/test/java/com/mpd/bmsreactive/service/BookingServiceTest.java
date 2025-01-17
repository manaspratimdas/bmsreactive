package com.mpd.bmsreactive.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import com.mpd.bmsreactive.domain.Booking;
import com.mpd.bmsreactive.repository.BookingRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBookings() {
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setReservation("John Doe");

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setReservation("Jane Doe");

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        Flux<Booking> bookings = bookingService.getAllBookings();

        StepVerifier.create(bookings)
                .expectNext(booking1)
                .expectNext(booking2)
                .verifyComplete();
    }

    @Test
    public void testGetBookingById() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setReservation("John Doe");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Mono<Booking> result = bookingService.getBookingById(1L);

        StepVerifier.create(result)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    public void testCreateBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setReservation("John Doe");

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Mono<Booking> result = bookingService.createBooking(booking);

        StepVerifier.create(result)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    public void testCheckRoomAvailability() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckIn(LocalDate.of(2025, 1, 20));
        booking.setCheckOut(LocalDate.of(2025, 1, 25));

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        Mono<Boolean> result = bookingService.checkRoomAvailability(LocalDate.of(2025, 1, 15),
                LocalDate.of(2025, 1, 18));

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    public void testCancelBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setReservation("John Doe");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Mono<Booking> result = bookingService.cancelBooking(1L);

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getCancellation().equals("Cancelled"))
                .verifyComplete();
    }

    @Test
    public void testUpdateBooking() {
        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        existingBooking.setReservation("John Doe");

        Booking updatedBooking = new Booking();
        updatedBooking.setReservation("Jane Doe");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);

        Mono<Booking> result = bookingService.updateBooking(1L, updatedBooking);

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getReservation().equals("Jane Doe"))
                .verifyComplete();
    }

}
