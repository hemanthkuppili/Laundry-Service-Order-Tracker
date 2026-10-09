package app;

import controller.LaundryController;
import repository.LaundryRepository;
import service.LaundryService;

public class Main {
    public static void main(String[] args) {
        LaundryService service = new LaundryService();
        LaundryRepository repository = new LaundryRepository();
        LaundryController controller = new LaundryController(service, repository);

        try {
            controller.start();
        } catch (Exception e) {
            System.out.println("Application error: " + e.getMessage());
        } finally {
            controller.close();
        }
    }
}
