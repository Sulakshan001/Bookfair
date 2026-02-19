# 📚 Book Fair – Stall Reservation Management System

This project is a stall reservation management system developed to simplify and enhance the process of stall booking and reservation management for the Colombo International Book Fair. The system streamlines vendor registration, stall allocation, QR-based entry pass generation, and organizer monitoring through separate vendor and employee portals.

Vendors can register their businesses, reserve available stalls via an interactive venue map, and receive a unique QR pass via email. Organizers can monitor stall availability, reservations, payments, and QR pass usage in real time.

---

## 📑 Table of Contents
- Features  
- Technologies Used  
- Getting Started  
- Acknowledgements  

---

# 🚀 Features

## Vendors / Publishers

### Vendor Registration
Vendors can register by providing:
- Name  
- Email  
- Password  
- Business Name  

Email verification is handled using an OTP-based system to ensure secure account creation.

### Stall Reservation
After logging in, vendors can:
- View an interactive exhibition venue map  
- Select stalls categorized as:
  - Small  
  - Medium  
  - Large  
- View available stalls normally  
- See reserved stalls grayed out  
- Reserve up to **3 stalls per business**  

A confirmation popup appears before finalizing the reservation.

### QR Pass Generation
Once a reservation is confirmed:
- A unique QR code is generated  
- The QR acts as the official exhibition entry pass  
- The QR code is sent via email  
- Vendors can download and use the QR for entry  

### Genre Selection
After reservation, vendors can:
- Add literary genres they will display/sell  
- Update genres from their dashboard  

### Payment Management
- Multiple payments can be recorded per reservation  
- Payment status tracking (Pending / Completed / Failed)  
- Reference number and payment details stored securely  

### Profile Management
Users can:
- Update personal details  
- Manage account settings  
- View reservation history  

---

## Employee Portal (Organizers)

### Secure Login
Employee-only login with role-based authentication using JWT.

### Stall Monitoring
Organizers can:
- View stall availability  
- View stall sizes and pricing  
- Monitor stall reservation status  

### Reservation Management
- View all vendor reservations  
- Track QR pass status  
- Monitor payment records  

### Email Tracking
- Track reservation confirmation emails  
- Monitor email delivery status  

---

## General Features

### Role-Based Authentication & Authorization
Secure authentication using JWT tokens ensures only authorized users (Vendor / Employee) can access specific functionalities.

### OTP-Based Password Recovery
Secure password reset using OTP email verification.

### Real-Time Data Updates
All updates related to reservations, payments, and QR passes are reflected immediately across the system.

---

# ⚙ Technologies Used

## Front-end
React  
Tailwind CSS  

**React :** Used to build responsive and interactive web applications.  
**Tailwind CSS :** Utility-first CSS framework for modern UI development.  

---

## Back-end
Spring Boot  
Hibernate  
MySQL  
JWT  
Java Mail API  

**Spring Boot :** Backend REST API development.  
**Hibernate :** ORM tool for managing database interactions.  
**MySQL :** Relational database management system.  
**JWT (JSON Web Tokens):** Secure authentication and authorization.  
**Java Mail API:** Sending reservation confirmation emails with QR codes.  

---

## Tools
Postman  
MySQL Workbench  
Git & GitHub  

**Postman:** API testing and debugging.  
**MySQL Workbench:** Database design and management.  
**Git & GitHub:** Version control and collaboration.  

---

# 📂 Getting Started

## 1. Clone the repository
---

## 2. Backend Setup

Navigate to the backend directory:
Configure the `application.properties` file with:
- MySQL database credentials  
- Email configuration settings  

Run the Spring Boot application:
Import the provided SQL files to set up the database schema.

---

## 3. Frontend Setup

Navigate to the frontend directory:
Install dependencies:
Start the React application:
# 🤝 Acknowledgements

We acknowledge the contributions of all team members in the successful completion of this project:

- Dayastan.T -https://github.com/dayastant
- Amillthan.K-https://github.com/amillthan
- Sulakshan.S-https://github.com/Sulakshan001
- Piranavi.S- https://github.com/Piranavi-Sasikaran
- Amshavarthana.S-https://github.com/Amshavarthana-S


---

© 2026 Software Architecture Group Project

