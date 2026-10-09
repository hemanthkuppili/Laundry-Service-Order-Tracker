package model;

public class LaundryOrder {
    public static final String PENDING = "PENDING";
    public static final String READY = "READY";
    public static final String COLLECTED = "COLLECTED";

    private static final String[] SERVICE_NAMES = {"Wash", "Dry", "Iron"};
    private static final int[] RATES = {50, 30, 20};

    private final int id;
    private final int customerId;
    private final int[] quantities;
    private String status;

    public LaundryOrder(int id, int customerId, int[] quantities, String status) {
        if (quantities == null || quantities.length != 3) {
            throw new IllegalArgumentException("Exactly 3 quantities are required.");
        }
        this.id = id;
        this.customerId = customerId;
        this.quantities = quantities.clone();
        this.status = status;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int[] getQuantities() { return quantities.clone(); }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int calculateCharge() {
        int charge = 0;
        for (int i = 0; i < quantities.length; i++) {
            charge += quantities[i] * RATES[i];
        }
        return charge;
    }

    public static String[] getServiceNames() { return SERVICE_NAMES.clone(); }
    public static int[] getRates() { return RATES.clone(); }
}
