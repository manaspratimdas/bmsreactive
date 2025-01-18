
#Booking Management System (BMS) - Reactive Application

## Introduction
The BMS Reactive Application is a Spring Boot-based system designed to manage bookings reactively. It leverages Spring WebFlux to provide non-blocking, asynchronous operations for handling booking-related requests. This documentation will guide you through the setup, usage, and functionality of the application.

## Prerequisites
- **Java** 17 or higher
- **Maven** 3.6 or higher
- An IDE such as **IntelliJ IDEA** or **Eclipse**
- A **PostgreSQL database** (or any JPA-compatible database)

## Setup Instructions

1. **Clone the Repository:**

   ```bash
   git clone <repository-url>
   cd bmsreactive
   ```

2. **Configure Database:**

   Update the `application.properties` file located in `src/main/resources` with your database credentials:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

3. **Build the Project:**

   Use Maven to build the project:

   ```bash
   mvn clean install
   ```

4. **Run the Application:**

   Start the application using:

   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Booking Management

#### Get All Bookings

- **Endpoint:** `GET /bookings`
- **Description:** Retrieve all bookings.
- **Response:** A list of bookings.

#### Get Booking by ID

- **Endpoint:** `GET /bookings/{id}`
- **Description:** Retrieve a booking by its ID.
- **Response:** Booking details.

#### Create a Booking

- **Endpoint:** `POST /bookings`
- **Description:** Create a new booking.
- **Request Body:** JSON representation of a Booking object.
- **Response:** The created booking.

#### Update a Booking

- **Endpoint:** `PUT /bookings/{id}`
- **Description:** Update an existing booking.
- **Request Body:** JSON representation of the updated Booking object.
- **Response:** The updated booking.

#### Delete a Booking

- **Endpoint:** `DELETE /bookings/{id}`
- **Description:** Delete a booking by its ID.
- **Response:** Void.

### Additional Features

#### Check Room Availability

- **Endpoint:** `GET /bookings/availability`
- **Description:** Check room availability for a given date range.
- **Query Parameters:** `checkIn` (LocalDate), `checkOut` (LocalDate)
- **Response:** Boolean indicating availability.

#### Cancel a Booking

- **Endpoint:** `POST /bookings/cancel/{id}`
- **Description:** Cancel a booking by its ID.
- **Response:** The updated booking with cancellation status.

#### Get Transformed Bookings

- **Endpoint:** `GET /bookings/transformed`
- **Description:** Retrieve all bookings with transformed reservation names.
- **Response:** A list of transformed bookings.

#### Merge Bookings

- **Endpoint:** `POST /bookings/merge`
- **Description:** Merge bookings from an external source.
- **Request Body:** Flux of Booking objects.
- **Response:** A merged list of bookings.

## Testing

The application includes unit and integration tests to ensure functionality:

### Run Unit Tests:

```bash
mvn test
```

### Run Integration Tests:

```bash
mvn verify
```

## Future Enhancements

- **Implement Date Range Booking Retrieval:** Uncomment and implement the `getBookingsWithinDateRange` method in `BookingService` to enable fetching bookings within a specified date range.
- **Enhance Security:** Integrate Spring Security for authentication and authorization.
- **Improve Error Handling:** Implement global exception handling for better error responses.

## Conclusion

This documentation provides a comprehensive guide to setting up and using the BMS Reactive Application. 
```
