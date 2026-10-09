package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import model.Customer;
import model.LaundryOrder;
import model.Staff;
import repository.LaundryRepository;
import service.LaundryService;

public class LaundryController {
    private final Scanner scanner = new Scanner(System.in);
    private final LaundryService service;
    private final LaundryRepository repository;
    private final Staff staff = new Staff(0, "Admin");

    public LaundryController(LaundryService service, LaundryRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    public void start() throws IOException {
        repository.createDataFolder();
        loadData();
        System.out.println("=================================");
        System.out.println(" Laundry Service Order Tracker");
        System.out.println("=================================");
        showMainMenu();
    }

    private void loadData() throws IOException {
        ArrayList<Customer> customers = repository.loadCustomers();
        ArrayList<LaundryOrder> orders = repository.loadOrders();
        service.getCustomers().addAll(customers);
        service.getOrders().addAll(orders);
        service.rebuildPhoneSet();
        service.updateNextCustomerId();
        service.updateNextOrderId();

        for (LaundryOrder order : orders) {
            if (service.findCustomer(order.getCustomerId()) == null) {
                throw new IOException("Order " + order.getId() + " references unknown customer.");
            }
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println();
            System.out.println("MAIN MENU");
            System.out.println("1. Staff menu");
            System.out.println("2. Customer menu");
            System.out.println("0. Exit");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> showStaffMenu();
                case 2 -> showCustomerMenu();
                case 0 -> { System.out.println("Thank you. Goodbye!"); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showStaffMenu() {
        while (true) {
            System.out.println();
            System.out.println("STAFF MENU");
            System.out.println("1. Register customer");
            System.out.println("2. View customers");
            System.out.println("3. Add laundry order");
            System.out.println("4. Update order status");
            System.out.println("5. View orders and reports");
            System.out.println("0. Back");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> registerCustomer();
                case 2 -> viewCustomers();
                case 3 -> addLaundryOrder();
                case 4 -> updateOrderStatus();
                case 5 -> showOrdersAndReport();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void registerCustomer() {
        System.out.println();
        System.out.println("REGISTER CUSTOMER");
        String name = readString("Enter name: ");
        String phone = readString("Enter phone: ");
        if (!phone.matches("\\d{10}")) {
            System.out.println("Phone must contain exactly 10 digits.");
            return;
        }
        Customer customer = service.registerCustomer(name, phone);
        if (customer == null) return;
        try {
            repository.saveCustomers(service.getCustomers());
            System.out.println("Customer " + customer.getId() + " saved.");
        } catch (IOException e) {
            service.getCustomers().remove(customer);
            service.rebuildPhoneSet();
            System.out.println("Could not save customer: " + e.getMessage());
        }
    }

    private void viewCustomers() {
        System.out.println();
        System.out.println("CUSTOMERS");
        ArrayList<Customer> customers = service.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println("ID: " + customer.getId() + " | Name: "
                    + customer.getName() + " | Phone: " + customer.getPhone());
        }
    }

    private void addLaundryOrder() {
        System.out.println();
        System.out.println("ADD LAUNDRY ORDER");
        if (service.getCustomers().isEmpty()) {
            System.out.println("No customers found. Register a customer first.");
            return;
        }
        int customerId = readInt("Customer ID: ");
        if (service.findCustomer(customerId) == null) {
            System.out.println("Customer not found.");
            return;
        }
        int wash = readQuantity("Wash quantity: ");
        int dry = readQuantity("Dry quantity: ");
        int iron = readQuantity("Iron quantity: ");
        int[] quantities = {wash, dry, iron};
        LaundryOrder order = service.addOrder(customerId, quantities);
        if (order == null) return;
        try {
            repository.saveOrders(service.getOrders());
            System.out.println("Order " + order.getId() + " saved.");
            System.out.println("Charge: ₹" + order.calculateCharge());
            System.out.println("Status: " + order.getStatus());
        } catch (IOException e) {
            service.getOrders().remove(order);
            System.out.println("Could not save order: " + e.getMessage());
        }
    }

    private void updateOrderStatus() {
        System.out.println();
        System.out.println("UPDATE ORDER STATUS");
        int orderId = readInt("Order ID: ");
        LaundryOrder order = service.findOrder(orderId);
        if (order == null) { System.out.println("Order not found."); return; }
        String oldStatus = order.getStatus();
        System.out.println("Current status: " + oldStatus);
        String newStatus;
        if (oldStatus.equals(LaundryOrder.PENDING)) newStatus = LaundryOrder.READY;
        else if (oldStatus.equals(LaundryOrder.READY)) newStatus = LaundryOrder.COLLECTED;
        else { System.out.println("Order is already COLLECTED."); return; }

        if (!service.updateStatus(orderId, newStatus)) return;
        try {
            repository.saveOrders(service.getOrders());
            System.out.println("Order status updated to " + newStatus);
        } catch (IOException e) {
            order.setStatus(oldStatus);
            System.out.println("Could not save status: " + e.getMessage());
        }
    }

    private void showOrdersAndReport() {
        System.out.println();
        System.out.println("ORDERS");
        ArrayList<LaundryOrder> orders = service.getOrders();
        if (orders.isEmpty()) {
            System.out.println("No records found.");
        } else {
            String[] serviceNames = LaundryOrder.getServiceNames();
            for (LaundryOrder order : orders) {
                int[] q = order.getQuantities();
                System.out.println("Order ID: " + order.getId());
                System.out.println("Customer ID: " + order.getCustomerId());
                System.out.println(serviceNames[0] + ": " + q[0] + " | "
                        + serviceNames[1] + ": " + q[1] + " | "
                        + serviceNames[2] + ": " + q[2]);
                System.out.println("Status: " + order.getStatus());
                System.out.println("Charge: ₹" + order.calculateCharge());
                System.out.println("-------------------------");
            }
        }
        System.out.println();
        String report = service.generateReport();
        System.out.println(report);
        String choice = readString("Save report to report.txt? (Y/N): ");
        if (choice.equalsIgnoreCase("Y")) {
            try {
                repository.saveReport(report);
                System.out.println("Report saved successfully.");
            } catch (IOException e) {
                System.out.println("Could not save report: " + e.getMessage());
            }
        }
    }

    private void showCustomerMenu() {
        System.out.println();
        int customerId = readInt("Enter customer ID: ");
        Customer customer = service.findCustomer(customerId);
        if (customer == null) { System.out.println("Customer not found."); return; }
        while (true) {
            System.out.println();
            System.out.println("CUSTOMER MENU");
            System.out.println("1. View my profile");
            System.out.println("2. View my orders");
            System.out.println("0. Back");
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> customer.showProfile();
                case 2 -> viewMyOrders(customerId);
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewMyOrders(int customerId) {
        boolean found = false;
        System.out.println();
        System.out.println("MY ORDERS");
        for (LaundryOrder order : service.getOrders()) {
            if (order.getCustomerId() == customerId) {
                found = true;
                System.out.println("Order " + order.getId() + " | ₹"
                        + order.calculateCharge() + " | " + order.getStatus());
            }
        }
        if (!found) System.out.println("No records found.");
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try { return Integer.parseInt(input); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid number."); }
        }
    }

    private String readString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
            } else if (input.contains("|") || input.contains("\n") || input.contains("\r")) {
                System.out.println("Invalid character.");
            } else return input;
        }
    }

    private int readQuantity(String message) {
        while (true) {
            int quantity = readInt(message);
            if (quantity >= 0 && quantity <= 50) return quantity;
            System.out.println("Quantity must be between 0 and 50.");
        }
    }

    public void close() {
        scanner.close();
    }
}
