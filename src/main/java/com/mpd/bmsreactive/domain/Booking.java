package com.mpd.bmsreactive.domain;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reservation;
    private String availability;
    private String cancellation;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Booking() {
    }

    public Booking(Long id, String reservation, String availability, String cancellation, LocalDate checkIn,
            LocalDate checkOut) {
        this.id = id;
        this.reservation = reservation;
        this.availability = availability;
        this.cancellation = cancellation;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getCancellation() {
        return cancellation;
    }

    public void setCancellation(String cancellation) {
        this.cancellation = cancellation;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "Booking [id=" + id + ", reservation=" + reservation + ", availability=" + availability
                + ", cancellation=" + cancellation + ", checkIn=" + checkIn + ", checkOut=" + checkOut + "]";
    }

}
