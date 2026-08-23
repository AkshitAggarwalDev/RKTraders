# 🛋️ RK Traders

### Premium Furniture & Home Decor E-Commerce Platform

> **“A production-oriented backend is not just about making APIs work — it is about making the system secure, predictable, maintainable, and ready to scale.”**

RK Traders is a full-stack e-commerce platform being built for a premium furniture and home-decor business.

The project is designed with a **backend-first engineering approach**, focusing on secure authentication, clean API architecture, data integrity, product management, cart and order workflows, reviews, and scalable API design.

**Current focus:** Backend engineering and API architecture
**Frontend:** Next.js + React + TypeScript — in progress
**Deployment:** In progress

---

## 🚀 What Makes RK Traders Different?

RK Traders is being developed beyond a basic CRUD application.

The backend addresses real e-commerce requirements such as:

* 🔐 JWT-based authentication and authorization
* 👤 Customer and owner roles
* 🔑 BCrypt password encryption
* 🛒 Cart ownership and stock validation
* 📦 Cart-to-order workflow
* ⭐ Customer reviews and rating aggregation
* 🖼️ Multiple product image uploads
* 🏷️ Category and product management
* 🔎 Product search and filtering
* 📊 Product rating summaries
* ⚠️ Centralized exception handling
* 🧱 Layered backend architecture
* 🗄️ Relational database persistence using MySQL

> **“The goal is not to build another CRUD application. The goal is to understand how real business rules translate into reliable software.”**

---

# 🏗️ Architecture

The backend follows a layered architecture:

```text
                    ┌─────────────────────┐
                    │      Client         │
                    │  Web / Frontend     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controller      │
                    │   REST API Layer    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       Service       │
                    │  Business Logic     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Repository      │
                    │   Data Access       │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      MySQL          │
                    │     Database        │
                    └─────────────────────┘
```

### Architectural Layers

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity / Database
```

DTOs are used where appropriate to keep API contracts separate from persistence entities.

---

# 🔐 Authentication & Security

Security is one of the core parts of the backend.

### Authentication Flow

```text
Client
   │
   │ Login
   ▼
Authentication Controller
   │
   ▼
Authentication Manager
   │
   ▼
User Verification
   │
   ▼
JWT Generation
   │
   ▼
Client receives JWT
   │
   ▼
Subsequent Requests
   │
   ▼
JwtFilter
   │
   ▼
JWT Validation
   │
   ▼
SecurityContextHolder
   │
   ▼
Authenticated Request
```

### Security Features

* JWT authentication
* Stateless authentication
* BCrypt password hashing
* Custom JWT filter
* Role-based authorization
* Owner authentication
* Customer authentication
* Protected endpoints
* Authentication context population
* Ownership validation for customer resources

A key design principle is:

> **“Authentication answers who you are. Authorization answers what you are allowed to do.”**

---

# 📦 Core Modules

## 1. Category Management

Implemented operations:

* Add category
* Get categories
* Update category
* Delete category
* Search categories
* Duplicate category validation
* Safe deletion when products exist
* Product count

---

## 2. Product Management

Implemented operations:

* Add product
* Get products
* Update product
* Delete product
* Search by product name
* Filter by category
* Filter by brand
* Price range filtering
* Sorting
* Out-of-stock handling
* `createdAt`
* `updatedAt`

---

## 3. Product Images

The platform supports:

* Multipart image upload
* Multiple images per product
* Primary image selection
* Image deletion
* Static resource serving
* UUID-based file names

UUID file naming avoids relying on user-provided filenames and reduces filename collision problems.

---

## 4. Cart Management

Implemented functionality:

* Add product to cart
* View cart
* Update quantity
* Remove item
* Clear cart
* Calculate cart total
* Automatic cart creation
* Duplicate product handling
* Stock validation
* Cart ownership validation

The cart is associated with the authenticated customer rather than trusting client-provided ownership information.

---

# 🛒 Order Management

The order workflow follows:

```text
Customer
   ↓
Cart
   ↓
Cart Validation
   ↓
Order Creation
   ↓
Order Items
   ↓
Order Total
```

The backend supports the transition from a customer's cart into an order.

Future improvements include:

* Order status lifecycle
* Payment integration
* Stock reservation
* Order cancellation
* Order history
* Transactional order processing

---

# ⭐ Review System

Customers can:

* Add reviews
* View product reviews
* Delete their own reviews

The backend also handles:

* Duplicate review prevention
* Average rating calculation
* Star distribution
* Review summary DTO

Example rating distribution:

```text
★★★★★  ███████████████  70%
★★★★☆  █████             20%
★★★☆☆  ██                  7%
★★☆☆☆                      2%
★☆☆☆☆                      1%
```

---

# ⚠️ Exception Handling

The project uses centralized exception handling through a global exception handler.

Custom exceptions include:

```text
ResourceNotFoundException
DuplicateResourceException
BadRequestException
```

This keeps error handling consistent across REST APIs instead of scattering repetitive exception logic throughout controllers.

---

# 🧩 DTO Architecture

DTOs are introduced to separate API contracts from database entities.

Examples:

```text
ReviewRequestDTO
ReviewSummaryResponseDTO
```

The project is gradually adopting DTOs across modules.

### Why DTOs?

```text
Database Entity
      ↓
    Service
      ↓
      DTO
      ↓
 REST Response
```

This provides:

* Better API contracts
* Reduced entity exposure
* Cleaner response models
* Easier future API evolution
* Better separation of concerns

---

# 🛠️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* Lombok
* Maven

### Database

* MySQL

### Development & Testing

* IntelliJ IDEA
* Git
* GitHub
* Postman

### Frontend — Planned / In Progress

* Next.js
* React
* TypeScript
* Tailwind CSS
* Framer Motion
* GSAP
* shadcn/ui

### Deployment — Planned

* Docker
* GitHub Actions
* Railway / Render
* Vercel

---

# 📁 Project Structure

```text
src/
└── main/
    └── java/
        └── ...
            ├── controller/
            ├── service/
            ├── repository/
            ├── entity/
            ├── dto/
            ├── exception/
            ├── security/
            └── config/
```

The structure follows separation of concerns so that controllers remain focused on HTTP handling while business rules remain inside the service layer.

---

# 🔄 Example Request Flow

For a protected customer request:

```text
HTTP Request
     ↓
JWT Token
     ↓
JwtFilter
     ↓
Token Validation
     ↓
SecurityContextHolder
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
MySQL
     ↓
DTO Response
     ↓
Client
```

This makes the authentication and business flow explicit rather than treating security as an afterthought.

---

# 🧪 API Testing

The backend APIs are tested using Postman.

Testing covers:

* Authentication
* Authorization
* CRUD operations
* Validation
* Error responses
* Cart workflows
* Product filtering
* Image uploads
* Reviews
* Order placement

---

# 🗺️ Development Roadmap

### ✅ Completed

* [x] Spring Boot backend
* [x] User authentication
* [x] JWT security
* [x] Role-based authorization
* [x] Category management
* [x] Product management
* [x] Product image management
* [x] Cart management
* [x] Order placement
* [x] Review system
* [x] Global exception handling
* [x] DTO adoption

### 🔄 In Progress

* [ ] Frontend development
* [ ] DTO standardization
* [ ] API pagination
* [ ] Swagger / OpenAPI
* [ ] Production configuration

### 🚀 Planned

* [ ] Dockerization
* [ ] CI/CD with GitHub Actions
* [ ] Cloud deployment
* [ ] Payment integration
* [ ] Order status lifecycle
* [ ] OAuth2
* [ ] Advanced observability
* [ ] Microservice evolution

---

# ⚙️ Local Setup

### Prerequisites

Make sure you have:

```text
Java 21+
Maven
MySQL
Git
```

### Clone Repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd RK-Traders
```

### Configure Database

Create a MySQL database and configure the application properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rk_traders
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Never commit production credentials or JWT secrets to GitHub.

Use environment variables for sensitive configuration.

---

# 🎯 Engineering Principles

The project is being developed around a few core principles:

### Separation of Concerns

Each layer has a clearly defined responsibility.

### Security by Design

Authentication, authorization, password encryption, ownership validation, and stateless security are considered part of the architecture.

### Data Integrity

Business rules such as duplicate prevention, stock validation, and safe deletion are enforced at the backend level.

### Maintainability

DTOs, service layers, centralized exceptions, and modular components are preferred over tightly coupled implementations.

### Scalability

Pagination, API documentation, Dockerization, and cloud deployment are part of the roadmap.

> **“Good software is not defined by how much code it contains, but by how clearly responsibility is divided within it.”**

---

# 📈 What I Learned Building RK Traders

Building RK Traders has been less about implementing endpoints and more about understanding how backend systems actually behave.

Key engineering concepts explored:

* Stateless authentication
* JWT lifecycle
* Spring Security filter chain
* SecurityContext
* Role-based authorization
* Entity relationships
* JPA/Hibernate persistence
* DTO-based API contracts
* Business-layer validation
* Resource ownership
* Multipart file handling
* Exception architecture
* Cart and order workflows
* Database-backed application design

---

# 👨‍💻 Project Status

**Backend:** 🟢 Core functionality complete
**Frontend:** 🟡 In development
**Deployment:** 🟡 Planned
**Production readiness:** 🟡 Iterating

RK Traders is an actively evolving project focused on turning backend concepts into a realistic e-commerce system.

---

## 📌 Final Note

> **“Build systems that work. Then build systems that are worth maintaining.”**

RK Traders is being developed with that philosophy — starting with a reliable backend foundation and progressively moving toward a complete, secure, scalable e-commerce platform.

---
