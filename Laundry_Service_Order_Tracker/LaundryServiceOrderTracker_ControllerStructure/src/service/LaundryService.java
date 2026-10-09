package service;

import java.util.ArrayList;
import java.util.HashSet;
import model.Customer;
import model.LaundryOrder;

public class LaundryService {
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<LaundryOrder> orders = new ArrayList<>();
    private final HashSet<String> phoneNumbers = new HashSet<>();
    private int nextCustomerId = 1;
    private int nextOrderId = 1;

    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<LaundryOrder> getOrders() { return orders; }

    public Customer registerCustomer(String name, String phone) {
        if (phoneNumbers.contains(phone)) {
            System.out.println("Phone number already exists.");
            return null;
        }
        Customer customer = new Customer(nextCustomerId++, name, phone);
        customers.add(customer);
        phoneNumbers.add(phone);
        return customer;
    }

    public LaundryOrder addOrder(int customerId, int[] quantities) {
        if (findCustomer(customerId) == null) {
            System.out.println("Customer not found.");
            return null;
        }
        if (quantities == null || quantities.length != 3) {
            System.out.println("Exactly 3 quantities are required.");
            return null;
        }
        int total = 0;
        for (int q : quantities) {
            if (q < 0 || q > 50) {
                System.out.println("Quantity must be between 0 and 50.");
                return null;
            }
            total += q;
        }
        if (total == 0) {
            System.out.println("At least one quantity must be positive.");
            return null;
        }
        LaundryOrder order = new LaundryOrder(nextOrderId++, customerId, quantities, LaundryOrder.PENDING);
        orders.add(order);
        return order;
    }

    public boolean updateStatus(int orderId, String newStatus) {
        LaundryOrder order = findOrder(orderId);
        if (order == null) return false;

        String current = order.getStatus();
        boolean allowed = (current.equals(LaundryOrder.PENDING) && newStatus.equals(LaundryOrder.READY))
                || (current.equals(LaundryOrder.READY) && newStatus.equals(LaundryOrder.COLLECTED));
        if (!allowed) return false;
        order.setStatus(newStatus);
        return true;
    }

    public Customer findCustomer(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) return customer;
        }
        return null;
    }

    public LaundryOrder findOrder(int id) {
        for (LaundryOrder order : orders) {
            if (order.getId() == id) return order;
        }
        return null;
    }

    public void rebuildPhoneSet() {
        phoneNumbers.clear();
        for (Customer customer : customers) phoneNumbers.add(customer.getPhone());
    }

    public void updateNextCustomerId() {
        int max = 0;
        for (Customer customer : customers) max = Math.max(max, customer.getId());
        nextCustomerId = max + 1;
    }

    public void updateNextOrderId() {
        int max = 0;
        for (LaundryOrder order : orders) max = Math.max(max, order.getId());
        nextOrderId = max + 1;
    }

    public String generateReport() {
        int pending = 0, ready = 0, collected = 0, sales = 0;
        for (LaundryOrder order : orders) {
            switch (order.getStatus()) {
                case LaundryOrder.PENDING -> pending++;
                case LaundryOrder.READY -> ready++;
                case LaundryOrder.COLLECTED -> { collected++; sales += order.calculateCharge(); }
                default -> { }
            }
        }
        return "REPORT\n"
                + "Customers: " + customers.size() + "\n"
                + "Orders: " + orders.size() + "\n"
                + "Pending: " + pending + "\n"
                + "Ready: " + ready + "\n"
                + "Collected: " + collected + "\n"
                + "Collected Sales: ₹" + sales;
    }
}
