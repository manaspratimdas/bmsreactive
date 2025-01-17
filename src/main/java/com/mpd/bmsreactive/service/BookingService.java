package com.mpd.bmsreactive.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpd.bmsreactive.domain.Booking;
import com.mpd.bmsreactive.repository.BookingRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Flux<Booking> getAllBookings() {
        return Flux.fromIterable(bookingRepository.findAll());
    }

    public Mono<Booking> getBookingById(Long id) {
        return Mono.justOrEmpty(bookingRepository.findById(id));
    }

    public Mono<Booking> createBooking(Booking booking) {
        return Mono.just(bookingRepository.save(booking));
    }

    public Mono<Void> deleteBooking(Long id) {
        bookingRepository.deleteById(id);
        return Mono.empty();
    }

    // Method to illustrate reactive programming

    // Check room availability using filter and collectList
    public Mono<Boolean> checkRoomAvailability(LocalDate checkIn, LocalDate checkOut) {
        return Flux.fromIterable(bookingRepository.findAll())
                .filter(booking -> booking.getCheckIn().isBefore(checkOut) && booking.getCheckOut().isAfter(checkIn))
                .collectList()
                .map(List::isEmpty);
    }

    // Cancel a booking using flatMap
    public Mono<Booking> cancelBooking(Long id) {
        return getBookingById(id)
                .flatMap(booking -> {
                    booking.setCancellation("Cancelled");
                    return Mono.just(bookingRepository.save(booking));
                });
    }

    // Update booking details using flatMap
    public Mono<Booking> updateBooking(Long id, Booking updatedBooking) {
        return getBookingById(id)
                .flatMap(existingBooking -> {
                    existingBooking.setReservation(updatedBooking.getReservation());
                    existingBooking.setAvailability(updatedBooking.getAvailability());
                    existingBooking.setCheckIn(updatedBooking.getCheckIn());
                    existingBooking.setCheckOut(updatedBooking.getCheckOut());
                    return Mono.just(bookingRepository.save(existingBooking));
                });
    }

    // New method to get all bookings and transform the result
    public Flux<Booking> getAllBookingsTransformed() {
        return Flux.fromIterable(bookingRepository.findAll())
                .transform(bookings -> bookings.map(booking -> {
                    booking.setReservation(booking.getReservation().toUpperCase());
                    return booking;
                }));
    }

    // New method to merge bookings from two different sources
    public Flux<Booking> mergeBookings(Flux<Booking> externalBookings) {
        return Flux.fromIterable(bookingRepository.findAll())
                .mergeWith(externalBookings);
    }

    // // New method to get bookings within a date range using flatMapMany
    // public Flux<Booking> getBookingsWithinDateRange(LocalDate startDate,
    // LocalDate endDate) {
    // return Flux.fromIterable(bookingRepository.findAll())
    // .flatMapMany(booking -> {
    // if (booking.getCheckIn().isAfter(startDate) &&
    // booking.getCheckOut().isBefore(endDate)) {
    // return Flux.just(booking);
    // } else {
    // return Flux.empty();
    // }
    // });
    // }

}
