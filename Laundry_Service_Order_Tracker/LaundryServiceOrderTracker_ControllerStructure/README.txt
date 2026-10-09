Laundry Service Order Tracker - Controller / Model / Repository / Service Structure

Project structure:
src/
  app/
    Main.java
  controller/
    LaundryController.java
  model/
    User.java
    Staff.java
    Customer.java
    LaundryOrder.java
  repository/
    LaundryRepository.java
  service/
    LaundryService.java

data/
  customers.txt
  orders.txt
  report.txt (created when report is saved)

Responsibilities:
- app: application entry point
- controller: console input, menus, and user interaction
- model: data/domain classes
- service: business logic and validation
- repository: file persistence and loading/saving data

JDK: 21

Compile:
javac -d out src/app/*.java src/controller/*.java src/model/*.java src/repository/*.java src/service/*.java

Run:
java -cp out app.Main

The original functionality is preserved while FileManager is reorganized as LaundryRepository and Main's menu logic is moved into LaundryController.
