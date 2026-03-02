# XND Finance

XND Finance is a personal finance management platform designed to centralize user accounts and financial transactions in a structured and secure environment. The system follows a RESTful architecture with a clear separation between backend and frontend layers.

---

## Core Features

### User Management
- User registration and profile updates  
- Input validation using DTOs  
- Secure authentication and authorization with Spring Security  

### Account Management
- Multiple account support (e.g., checking, savings)  
- Balance tracking per account  
- Controlled balance updates via transactions  

### Transaction Management
- Income and expense registration  
- Automatic balance recalculation  
- Business rules enforcement (e.g., insufficient balance protection)  
- Timestamped transaction history  

---

## Architecture

The project follows a layered architecture:

- **Controller Layer** — REST endpoints  
- **Service Layer** — Business rules and validation  
- **Repository Layer** — JPA data access  
- **DTO Layer** — Request/response isolation  
- **Exception Handling** — Centralized API error responses  

The backend is designed with clear separation of concerns and validation at both request and entity levels.

---

## Technology Stack

### Backend
- Java 17  
- Spring Boot 3.3.5  
- Spring Security  
- Spring Data JPA / Hibernate  
- PostgreSQL  
- Gradle  
- JUnit 5  

### Frontend
- React  
- TypeScript  
- Axios for API communication  
- Context API / Hooks for state management  

The frontend consumes the REST API and maintains separation between UI components and service integrations.

---

## Security Model

- Authentication with Spring Security  
- Endpoint protection (role-based access control in progress)  
- Planned JWT-based stateless authentication  

---

## Project Status

**Under Active Development**

Current focus areas:
- Refinement of service-layer validation  
- Improved exception handling strategy  
- Expansion of test coverage  
- Security hardening  

---

## Roadmap

- JWT authentication  
- Transaction analytics and reporting  
- Budget management module  
- Investment tracking  
- Frontend UX improvements  
- Containerization with Docker  

---

## Purpose

This is an educational project focused on deepening knowledge in:

- Spring Boot architecture  
- REST API design  
- Validation and business rule enforcement  
- Secure backend development  
- Full-stack integration
