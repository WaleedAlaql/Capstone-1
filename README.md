# E-Commerce Backend Management System

A robust and secure backend e-commerce system built with Java 17 and Spring Boot 3, providing a comprehensive set of RESTful APIs and advanced business logic operations.

## Architecture & Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3
- **Build Tool:** Maven
- **Architecture:** Layered Architecture (Controller, Service, Repository/Model)
- **Validation:** Jakarta Validation
- **Database Simulation:** In-memory Collections (ArrayList / Mock Database)

---

## Core Features & Endpoints

### 1. Product Discount System
- **Endpoint:** `PUT /api/v1/product/discount/{categoryId}/{percentage}`
- **Description:** Dynamically applies a percentage-based discount to all products belonging to a specific category simultaneously.

### 2. User Balance Deposit
- **Endpoint:** `PUT /api/v1/user/deposit/{userId}/{amount}`
- **Description:** Safely tops up a user's wallet balance with positive amount validation.

### 3. Admin Gift Card System
- **Endpoint:** `PUT /api/v1/user/gift-card/{adminId}/{targetUserId}/{amount}`
- **Description:** Verifies administrative authorization and issues promotional reward credit directly to a customer's wallet.

### 4. Balance-Based Product Recommendation
- **Endpoint:** `GET /api/v1/user/recommend/{userId}`
- **Description:** Filters and returns all available products that match or fall below the user's current wallet balance.

### 5. Product Return & Refund System
- **Endpoint:** `POST /api/v1/user/refund/{userId}/{productId}/{merchantId}`
- **Description:** Reverses a purchase transaction by restoring product stock to the merchant and refunding the exact price to the user's wallet.

### 6. Admin Privilege Control (Delete Customer)
- **Endpoint:** `DELETE /api/v1/user/admin-delete/{adminId}/{targetUserId}`
- **Description:** Enforces strict role-based access control ensuring only authorized administrators can delete customer accounts.

---

## Project Structure

```text
src/
├── controller/    # REST Controllers handling incoming HTTP requests
├── model/         # Domain entities (User, Product, Category, Merchant, MerchantStock)
└── service/       # Business logic and core validation implementations
