# UHAuction Backend

A Spring Boot backend service for an online auction platform supporting UTAR Hospital fundraising initiatives.

## 🎯 Project Overview

UHAuction is a web-based auction platform designed to facilitate fundraising for UTAR Hospital. The platform enables users to list items for auction, place bids, and participate in a secure and transparent bidding process.

## 🚀 Features

- **User Management**
  - User registration and authentication
  - Role-based access control (Admin/User)
  - Profile management

- **Auction Management**
  - Create and manage auction listings
  - Upload multiple images for items
  - Set auction duration and starting price
  - Real-time bidding updates via WebSocket

- **Bidding System**
  - Place and track bids
  - Automatic bid validation
  - Real-time notifications
  - Auction history tracking

- **Security**
  - JWT-based authentication
  - Password encryption
  - CORS protection
  - Input validation

## 🛠 Technology Stack

- **Framework:** Spring Boot
- **Database:** MySQL
- **Security:** Spring Security + JWT
- **Real-time Communication:** WebSocket (STOMP)
- **Documentation:** Swagger/OpenAPI
- **Build Tool:** Maven

## 📋 Prerequisites

- JDK 8
- Maven 3.6+
- MySQL 8.0+

## 🔧 Configuration

1. **Server Configuration**
   ```yaml
   server:
     port: 8000
   
   web:
     domain: http://localhost
   ```

2. **Database Configuration**
   ```yaml
   spring:
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       username: your_username
       password: your_password
       url: jdbc:mysql://localhost:3306/uhauction?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=GMT%2B8
       type: com.zaxxer.hikari.HikariDataSource
   ```

3. **Email Configuration**
   ```yaml
   spring:
     mail:
       host: smtp.gmail.com
       port: 587
       username: your-email@gmail.com
       password: your-app-password
       properties:
         mail:
           smtp:
             auth: true
             starttls:
               enable: true
   ```

4. **Redis Configuration**
   ```yaml
   spring:
     redis:
       host: your_ip_address
       port: your_port_number
       password: your_password
       database: 0
       timeout: 10000
       lettuce:
         pool:
           max-active: 8
           max-wait: -1
           max-idle: 8
           min-idle: 0
   ```

5. **MinIO Configuration**
   ```yaml
   minio:
     endpoint: http://localhost:9000
     access-key: root
     secret-key: "12345678"
     bucket-name: uhauction
   ```

6. **File Upload Configuration**
   ```yaml
   spring:
     servlet:
       multipart:
         max-file-size: 50MB
         max-request-size: 50MB
   ```

7. **Logging Configuration**
   ```yaml
   logging:
     level:
       root: info
       com.utar.uhauction: debug
   ```

> **Note:** 
> - Replace sensitive information (passwords, keys, etc.) with your own values
> - The configuration above is for development environment
> - For production, use appropriate values and ensure security measures
> - Store sensitive information using environment variables or secure vaults in production

## Additional Requirements

- Redis server running on specified host and port
- MinIO server for object storage
- SMTP server access for email functionality



## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/UHAuction-backend.git
   cd UHAuction-backend
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The server will start at `http://localhost:8080`

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

