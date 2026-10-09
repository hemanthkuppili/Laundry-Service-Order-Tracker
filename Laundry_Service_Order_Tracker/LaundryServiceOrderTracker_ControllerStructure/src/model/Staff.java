package model;

public class Staff extends User {
    public Staff(int id, String name) {
        super(id, name);
    }

    @Override
    public void showProfile() {
        System.out.println("Staff ID: " + id);
        System.out.println("Name: " + name);
    }
}
