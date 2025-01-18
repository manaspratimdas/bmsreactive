Feature: Booking Management

  Scenario: Create a new booking
    Given the booking details
    When the client sends a POST request to "/bookings"
    Then the booking is created successfully

  Scenario: Get all bookings
    Given there are bookings in the system
    When the client sends a GET request to "/bookings"
    Then the client receives a list of bookings

  Scenario: Check room availability
    Given there are bookings in the system
    When the client sends a GET request to "/bookings/availability" with check-in and check-out dates
    Then the client receives the room availability status

  Scenario: Cancel a booking
    Given there is a booking in the system
    When the client sends a POST request to "/bookings/cancel/{id}"
    Then the booking is cancelled successfully

  Scenario: Update a booking
    Given there is a booking in the system
    When the client sends a PUT request to "/bookings/1" with updated booking details
    Then the booking is updated successfully