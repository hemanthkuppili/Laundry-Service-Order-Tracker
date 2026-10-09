package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import model.Customer;
import model.LaundryOrder;

public class LaundryRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path CUSTOMER_FILE = DATA_DIR.resolve("customers.txt");
    private static final Path ORDER_FILE = DATA_DIR.resolve("orders.txt");
    private static final Path REPORT_FILE = DATA_DIR.resolve("report.txt");

    public void createDataFolder() throws IOException {
        Files.createDirectories(DATA_DIR);
        if (!Files.exists(CUSTOMER_FILE)) Files.createFile(CUSTOMER_FILE);
        if (!Files.exists(ORDER_FILE)) Files.createFile(ORDER_FILE);
    }

    public ArrayList<Customer> loadCustomers() throws IOException {
        ArrayList<Customer> customers = new ArrayList<>();
        if (!Files.exists(CUSTOMER_FILE)) return customers;

        try (BufferedReader reader = Files.newBufferedReader(CUSTOMER_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split("\\|", -1);
                if (fields.length != 3) continue;
                try {
                    customers.add(new Customer(Integer.parseInt(fields[0]), fields[1], fields[2]));
                } catch (NumberFormatException ignored) { }
            }
        }
        return customers;
    }

    public ArrayList<LaundryOrder> loadOrders() throws IOException {
        ArrayList<LaundryOrder> orders = new ArrayList<>();
        if (!Files.exists(ORDER_FILE)) return orders;

        try (BufferedReader reader = Files.newBufferedReader(ORDER_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split("\\|", -1);
                if (fields.length != 4) continue;
                String[] q = fields[2].split(",", -1);
                if (q.length != 3) continue;
                try {
                    int[] quantities = {
                            Integer.parseInt(q[0]),
                            Integer.parseInt(q[1]),
                            Integer.parseInt(q[2])
                    };
                    String status = fields[3];
                    if (!status.equals(LaundryOrder.PENDING)
                            && !status.equals(LaundryOrder.READY)
                            && !status.equals(LaundryOrder.COLLECTED)) continue;
                    orders.add(new LaundryOrder(Integer.parseInt(fields[0]),
                            Integer.parseInt(fields[1]), quantities, status));
                } catch (NumberFormatException ignored) { }
            }
        }
        return orders;
    }

    public void saveCustomers(ArrayList<Customer> customers) throws IOException {
        Path temp = DATA_DIR.resolve("customers.tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
            for (Customer customer : customers) {
                writer.write(customer.getId() + "|" + customer.getName() + "|" + customer.getPhone());
                writer.newLine();
            }
        }
        replace(temp, CUSTOMER_FILE);
    }

    public void saveOrders(ArrayList<LaundryOrder> orders) throws IOException {
        Path temp = DATA_DIR.resolve("orders.tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
            for (LaundryOrder order : orders) {
                int[] q = order.getQuantities();
                writer.write(order.getId() + "|" + order.getCustomerId() + "|"
                        + q[0] + "," + q[1] + "," + q[2] + "|" + order.getStatus());
                writer.newLine();
            }
        }
        replace(temp, ORDER_FILE);
    }

    public void saveReport(String report) throws IOException {
        Files.writeString(REPORT_FILE, report, StandardCharsets.UTF_8);
    }

    private void replace(Path temp, Path target) throws IOException {
        try {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
