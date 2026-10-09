# 🧺 Laundry Service Order Tracker

A Java console-based application designed to manage customer records, laundry orders, order statuses, and sales reports. This project demonstrates Object-Oriented Programming (OOP), Java Collections, input validation, layered architecture, and file handling.

## 📌 Project Overview

The Laundry Service Order Tracker helps laundry staff register customers, create orders, calculate service charges, track order progress, and generate reports. Customer and order data are stored in text files so they can be loaded when the application starts again.

## ✨ Features

- **Customer Management:** Register customers and view customer records.
- **Unique Phone Numbers:** Validate 10-digit phone numbers and prevent duplicates.
- **Laundry Order Management:** Create orders for Wash, Dry, and Iron services.
- **Automatic Billing:** Calculate order charges using fixed service rates.
- **Order Tracking:** Track orders through `PENDING`, `READY`, and `COLLECTED`.
- **Reports:** View customer totals, order counts, status counts, and collected sales.
- **File Handling:** Save and load customer and order information.
- **Input Validation:** Handle invalid menu choices and quantities.

## 🛠️ Technologies Used

- Java (JDK 21)
- Object-Oriented Programming
- ArrayList
- HashSet
- Arrays
- File Handling
- Layered Architecture

## 🏗️ Project Structure

```text
LaundryServiceOrderTracker/
├── src/
│   ├── app/
│   │   └── Main.java
│   ├── controller/
│   │   └── LaundryController.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Staff.java
│   │   ├── Customer.java
│   │   └── LaundryOrder.java
│   ├── repository/
│   │   └── LaundryRepository.java
│   └── service/
│       └── LaundryService.java
├── data/
│   ├── customers.txt
│   ├── orders.txt
│   └── report.txt
└── README.md
```

## 🧩 Architecture

The application uses a layered architecture to separate responsibilities.

| Layer | Responsibility |
|---|---|
| `app` | Starts the application |
| `controller` | Handles menus and user interaction |
| `model` | Represents users, customers, staff, and orders |
| `service` | Handles business logic, validation, and calculations |
| `repository` | Handles loading and saving data |

## 💰 Laundry Service Rates

| Service | Rate |
|---|---:|
| Wash | ₹50 |
| Dry | ₹30 |
| Iron | ₹20 |

**Example:** For 2 Wash, 1 Dry, and 3 Iron:

`(2 × 50) + (1 × 30) + (3 × 20) = ₹190`

Each service quantity must be between 0 and 50, and at least one quantity must be greater than zero.

## 🔄 Order Status Workflow

```text
PENDING → READY → COLLECTED
```

New orders begin with `PENDING`. Staff can update them to `READY` and then `COLLECTED`.

## 🚀 How to Run the Project

### Prerequisites

Install **JDK 21** and ensure `java` and `javac` are available in your terminal.

Check your Java version:

```bash
java -version
javac -version
```

### 1. Clone the repository

Replace the URL below with your GitHub repository URL.

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
```

### 2. Open the project folder

```bash
cd YOUR_REPOSITORY
```

### 3. Compile the Java files

Run this command from the project root directory:

```bash
javac -d out src/model/*.java src/service/*.java src/repository/*.java src/controller/*.java src/app/*.java
```

### 4. Run the application

```bash
java -cp out app.Main
```

## 📋 Main Menu

The application provides the following options:

1. Staff Menu
2. Customer Menu
0. Exit

### Staff Menu

- Register customer
- View customers
- Add laundry order
- Update order status
- View orders and reports

### Customer Menu

- View personal profile
- View personal orders

## 💾 Data Storage

The application uses text files for persistent storage:

- `customers.txt` — stores customer IDs, names, and phone numbers.
- `orders.txt` — stores order IDs, customer IDs, service quantities, and statuses.
- `report.txt` — stores generated reports.

The application loads saved customer and order data when it starts.

## 🎓 Concepts Demonstrated

- **Abstraction:** The `User` abstract class defines common user behavior.
- **Inheritance:** `Staff` and `Customer` extend `User`.
- **Encapsulation:** Classes control access to their data through fields and methods.
- **Polymorphism:** `showProfile()` is overridden in the `Staff` and `Customer` classes.
- **Collections:** `ArrayList` stores customers and orders; `HashSet` helps prevent duplicate phone numbers.
- **File Handling:** Customer and order records are saved and loaded from text files.
- **Layered Architecture:** Separates user interaction, business logic, data models, and storage.

## 🔮 Future Enhancements

- Database integration using MySQL
- Graphical user interface or web application
- Secure staff and customer authentication
- Online payment integration
- SMS or email notifications
- Advanced reports and analytics

## 👨‍💻 Author

**Kuppili Hemanth**

Java Project | Laundry Service Order Tracker

---

*This project was developed for learning and demonstrating core Java programming concepts through a practical application.*
