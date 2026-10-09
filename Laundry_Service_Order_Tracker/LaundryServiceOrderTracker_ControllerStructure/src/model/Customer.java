package model;

public class Customer extends User {
    private final String phone;

    public Customer(int id, String name, String phone) {
        super(id, name);
        this.phone = phone;
    }

    public String getPhone() { return phone; }

    @Override
    public void showProfile() {
        System.out.println("Customer ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
    }
}
