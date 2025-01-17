package com.mpd.bmsreactive.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mpd.bmsreactive.domain.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
