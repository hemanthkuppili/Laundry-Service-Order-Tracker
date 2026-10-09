package model;

public abstract class User {
    protected final int id;
    protected final String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public abstract void showProfile();
}
