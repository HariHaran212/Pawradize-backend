# ☕ Pawradise - Pet Hub Backend

This repository contains the backend REST API for "Pawradise," a full-featured pet hub application. It is built with Spring Boot and provides all the necessary services for e-commerce, pet adoption, user management, and more.

[![Java Version](https://img.shields.io/badge/java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/database-MongoDB-green.svg)](https://www.mongodb.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---
## ## Features

* **Secure REST API**: Stateless architecture secured with JWT (JSON Web Tokens).
* **Hybrid Authentication**: Supports both local (email/password) registration and social login with Google (OAuth2).
* **Role-Based Access Control (RBAC)**: Granular permissions for `USER`, `SUPER_ADMIN`, `STORE_MANAGER`, and `ADOPTION_COORDINATOR` roles using Spring Security.
* **Full CRUD Operations**: Endpoints for managing Products, Pets, Orders, Users, and Content.
* **Transactional Logic**: Ensures data integrity for critical operations like order creation and stock management.
* **File Uploads**: Handles multipart file uploads for user avatars and product/pet images.
* **Scheduled Tasks**: Automated background job for anonymizing user data after an account deactivation grace period.
* **Email Service**: Integrated email sending for OTP verification and user notifications.

---
## ## Tech Stack

* **Framework**: [Spring Boot 3.x](https://spring.io/)
* **Language**: Java 17
* **Database**: [MongoDB](https://www.mongodb.com/) (with Spring Data MongoDB)
* **Security**: [Spring Security 6](https://spring.io/projects/spring-security) (JWT & OAuth2)
* **Build Tool**: Maven
* **API Documentation**: SpringDoc OpenAPI (Swagger UI)

---
## ## Getting Started

### **Prerequisites**

* Java JDK 17 or later
* Apache Maven
* A running MongoDB instance (local or a cloud service like MongoDB Atlas)

### **Installation & Setup**

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/pawradise-backend.git](https://github.com/your-username/pawradise-backend.git)
    cd pawradise-backend
    ```

2.  **Configure Environment Variables:**
    This application uses environment variables for sensitive data. Set these in your IDE's run configuration or as OS environment variables.

    ```
    # MongoDB Connection
    MONGO_DB_URI=mongodb+srv://<user>:<password>@cluster...

    # JWT Configuration
    JWT_SECRET_KEY=your-long-random-super-secret-string-for-jwt

    # Google OAuth2 Credentials
    GOOGLE_CLIENT_ID=your-google-client-id.apps.googleusercontent.com
    GOOGLE_CLIENT_SECRET=your-google-client-secret

    # Email Service (Example for Gmail)
    GMAIL_USERNAME=your.email@gmail.com
    GMAIL_APP_PASSWORD=your-16-character-google-app-password
    ```

3.  **Build and Run the application:**
    ```bash
    # Using Maven Wrapper
    ./mvnw spring-boot:run
    ```
    The API server will start on `http://localhost:2025`.

---

## 🚀 Frontend

The frontend for this application is deployed separately and is available for you to view and interact with.

Check it out here: [pawradize.vercel.app](https://pawradize.vercel.app)

[//]: # (---)

[//]: # (## ## License)

[//]: # (This project is licensed under the MIT License. See the [LICENSE]&#40;LICENSE&#41; file for details.)
