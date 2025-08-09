# Movie Reservation API

## Description
A straightforward and efficient RESTful backend built with Spring Boot for managing cinema operations:
- Manage movies and their showtimes
- Reserve seats and process reservations
- Handle user accounts with registration, login, email-based activation, and password reset features  

## Features
- Movies management: create, update, list, and archive movies
- Showtimes: schedule showtimes and list by movie/date
- Seat reservations: reserve/cancel seats with basic availability checks
- Booking flow: create reservations and view user reservation history
- Authentication & authorization: register, login, logout, CSRF protection via cookie, role-based access
- Account lifecycle: email-based account activation and password reset (HTML templates included)
- Persistence & caching: PostgreSQL for data, Redis for sessions/caching
- Mail sandbox: MailDev via Docker Compose for local email testing
- Validation & error handling: request validation with descriptive errors
- Environment-ready: Docker Compose services and sensible dev defaults

## Database Design
<p align="center">
  <img src="docs/drawSQL-image-export-2025-04-27.png" alt="Database Design" width="600"/>
</p>
