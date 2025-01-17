package com.mpd.bmsreactive;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mpd.bmsreactive.domain.Booking;
import com.mpd.bmsreactive.service.BookingService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public Flux<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Mono<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public Mono<Booking> createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }

    // reactive call
    // GET request to
    // GET request to
    // http://localhost:8080/bookings/availability?checkIn=2025-01-20&checkOut=2025-01-25

    @GetMapping("/availability")
    public Mono<Boolean> checkRoomAvailability(@RequestParam LocalDate checkIn, @RequestParam LocalDate checkOut) {
        return bookingService.checkRoomAvailability(checkIn, checkOut);
    }

    // http://localhost:3000/bookings/cancel/{id}
    // http://localhost:8080/bookings/cancel/1

    @PostMapping("/cancel/{id}")
    public Mono<Booking> cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    // http://localhost:3000/bookings/1 (replace 1 with the actual booking ID)
    @PutMapping("/{id}")
    public Mono<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return bookingService.updateBooking(id, updatedBooking);
    }

    @GetMapping("/transformed")
    public Flux<Booking> getAllBookingsTransformed() {
        return bookingService.getAllBookingsTransformed();
    }

    @PostMapping("/merge")
    public Flux<Booking> mergeBookings(@RequestBody Flux<Booking> externalBookings) {
        return bookingService.mergeBookings(externalBookings);
    }

    // @GetMapping("/date-range")
    // public Flux<Booking> getBookingsWithinDateRange(@RequestParam LocalDate
    // startDate, @RequestParam LocalDate endDate) {
    // return bookingService.getBookingsWithinDateRange(startDate, endDate);
    // }

}
