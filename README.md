# JAVA-Store-ERP

### Table of Contents

- [Project Description](#project-description)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [RESTful API Endpoints](#restful-api-endpoints)

### Project Description

JAVA-Store-ERP is a comprehensive Enterprise Resource Planning (ERP) system created for store management. It efficienty
handles products, inventory, sales, and customer data through a robust backend built with Java and Spring Boot.

### Features

- **Product Management**: Manage product details including creation, updating, and deletion.
- **Inventory Management**: Track and update inventory items.
- **Order Management**: Handle orders seamlessly.
- **Reservation System**: Contains an integrated reservation system.
- **Customer Management**: Maintain customer-order information.
- **Invoice Generation**: Automatically generates invoices when users pay for their orders and reservations based on
  accounting states.
- **Asynchronous Processing**: Leveraging RabbitMQ for decoupled and scalable message-driven architecture.

### Technologies Used

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **RabbitMQ**
- **RESTful API**
- **MySQL**
- **Docker**

### Architecture

The project follows a modular and layered architecture, utilizing RESTful principles to expose a clean API for
interaction. RabbitMQ is integrated for handling asynchronous tasks, ensuring the system remains responsive under heavy
load.

### Installation

To run this project locally, follow these steps:

1. **Clone the repository:**
    ```bash
    git clone https://github.com/djordje34/JAVA-Store-ERP.git
    cd JAVA-Store-ERP
    ```

2. **Configure the database:**
   Update the `application.properties` file to match your database configuration.

3. **Build the project:**
   Ensure you have JDK 21 or higher installed. Then build the project using Maven.
    ```bash
    mvn clean install
    ```
4. **Run the application:**
    ```bash
    java -jar target/JAVA-Store-ERP.jar
    ```

### Usage

After installing and running the application, you can access the ERP system through your web browser
at `http://localhost:8080`.

### RESTful API Endpoints

#### Basic API Endpoints

- Product Endpoints (CRUD):
  ```/api/products/...```
- Customer Endpoints (CRUD):
  ```/api/customers/...```
- Inventory Item Endpoints (CRUD):
  ```/api/inventory_items/...```
- Accounting Endpoints (CRUD):
  ```/api/accountings/...```
- Invoice Endpoints (CRUD):
  ```/api/invoices/...```
- Order Endpoints (CRUD):
  ```/api/orders/...```
- Order Item Endpoints (CRUD):
  ```/api/order_items/...```
- Reservation Endpoints (CRUD):
  ```/api/reservations/...```
- Supplier Endpoints (CRUD):
  ```/api/suppliers/...```
- Warehouse Endpoints (CRUD):
  ```/api/warehouses/...```

#### Advanced API Endpoints

- Reception of Products Endpoint (POST):
  ```/api/advancedGoods/receptionOfProducts/```
- Product Price Estimation Endpoint (GET):
  ```/api/advancedGoods/getProductPrice?id=2&factor=1.2 //for example```
- Place Order Endpoint (POST):
  ```/api/advancedSales/placeOrder/```
- Pay for Order (Accounting) Endpoint (POST):
  ```/api/advancedSales/payAccounting/```
