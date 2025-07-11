package pharmacy;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Medication {
    private String name;
    private String id;
    private double price;
    private int stock;
    private double rating;  // Store the average rating

    public Medication(String name, String id, double price, int stock) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.rating = -1; // Unrated initially
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void updateStock(int amount) {
        this.stock += amount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", rating=" + (rating == -1 ? "No rating yet" : rating) +
                '}';
    }
}

class Order {
    private Medication medication;
    private int quantity;
    private double totalCost;
    private String address;  // Store the address for the order
    private boolean isCanceled;  // Flag to track if the order is canceled

    public Order(Medication medication, int quantity) {
        this.medication = medication;
        this.quantity = quantity;
        this.totalCost = medication.getPrice() * quantity;
        this.address = ""; // Initialize address as empty
        this.isCanceled = false;  // Orders are not canceled initially
    }

    public Medication getMedication() {
        return medication;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel() {
        this.isCanceled = true;
        this.medication.updateStock(quantity);  // Restores the stock when canceled
    }

    @Override
    public String toString() {
        return "Order{" +
                "medication=" + medication.getName() +
                ", quantity=" + quantity +
                ", totalCost=$" + totalCost +
                ", deliveryAddress='" + address + '\'' +
                ", canceled=" + isCanceled +
                '}';
    }
}

class PharmacyManagementSystem {
    private List<Medication> medications;
    private List<Order> orders;  // List to store orders placed by the user

    public PharmacyManagementSystem() {
        medications = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public void addMedication(String name, String id, double price, int stock) {
        medications.add(new Medication(name, id, price, stock));
        System.out.println("Medication added: " + name);
    }

    public void updateStock(String id, int amount) {
        for (Medication medication : medications) {
            if (medication.getId().equals(id)) {
                medication.updateStock(amount);
                System.out.println("Stock updated for " + medication.getName() + ": " + medication.getStock());
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    public void displayMedications() {
        System.out.println("Current Medications:");
        for (Medication medication : medications) {
            System.out.println(medication);
        }
    }

    public void findMedication(String id) {
        for (Medication medication : medications) {
            if (medication.getId().equals(id)) {
                System.out.println("Medication found: " + medication);
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    public void orderMedication(String id, int quantity, Scanner scanner) {
        for (Medication medication : medications) {
            if (medication.getId().equals(id)) {
                if (medication.getStock() >= quantity) {
                    medication.updateStock(-quantity);
                    double totalCost = medication.getPrice() * quantity;
                    System.out.println("Order placed for " + quantity + " of " + medication.getName() + ". Remaining stock: " + medication.getStock());
                    System.out.println("Total cost of the order: $" + totalCost);

                    // Create and add the order to the list
                    Order newOrder = new Order(medication, quantity);
                    orders.add(newOrder);

                    // Ask for the delivery address
                    System.out.print("Please enter your delivery address: ");
                    scanner.nextLine(); // Consume newline
                    String address = scanner.nextLine();
                    newOrder.setAddress(address);

                    // Ask for the rating
                    System.out.print("Please rate this medication out of 5: ");
                    double rating = scanner.nextDouble();
                    if (rating >= 1 && rating <= 5) {
                        medication.setRating(rating);
                        System.out.println("Thank you for your feedback! Rating updated.");
                    } else {
                        System.out.println("Invalid rating. Please provide a rating between 1 and 5.");
                    }
                } else {
                    System.out.println("Not enough stock available.");
                }
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    // Method to display orders placed
    public void displayOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders have been placed yet.");
        } else {
            System.out.println("Orders placed:");
            for (Order order : orders) {
                if (!order.isCanceled()) {  // Only display non-canceled orders
                    System.out.println(order);
                }
            }
        }
    }

    // Method to cancel an order
    public void cancelOrder(Scanner scanner) {
        if (orders.isEmpty()) {
            System.out.println("No orders to cancel.");
            return;
        }

        System.out.println("List of placed orders:");
        // List only non-canceled orders
        List<Order> activeOrders = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            if (!order.isCanceled()) {
                activeOrders.add(order);
                System.out.println((activeOrders.size()) + ". " + order);
            }
        }

        if (activeOrders.isEmpty()) {
            System.out.println("No active orders to cancel.");
            return;
        }

        System.out.print("Enter the order number to cancel: ");
        int orderNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (orderNumber > 0 && orderNumber <= activeOrders.size()) {
            Order orderToCancel = activeOrders.get(orderNumber - 1);
            orderToCancel.cancel();
            orders.remove(orderToCancel);  // Remove the canceled order from the list
            System.out.println("Order canceled successfully. Stock has been restored.");
        } else {
            System.out.println("Invalid order number.");
        }
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

public class ManagementSystem {

    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    // Login method
    public static boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    // Register method
    public static void register(String username, String password) {
        users.add(new User(username, password));
        System.out.println("Registration successful!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PharmacyManagementSystem pms = new PharmacyManagementSystem();

        // This loop will handle login or register.
        while (currentUser == null) {
            System.out.println("1. Login\n2. Register");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (option == 1) {
                // Login
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                if (login(username, password)) {
                    System.out.println("Login successful!");
                    break; // Exit the login/register loop
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }
            } else if (option == 2) {
                // Register
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                register(username, password);
                System.out.println("Please login with your new credentials.");
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        // Menu for the logged-in user
        while (true) {
            System.out.println("\n1. Add Medication\n2. Update Stock\n3. Display Medications\n4. Find Medication\n5. Order Medication\n6. Display Orders\n7. Cancel Order\n8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add new medication using user input
                    System.out.print("Enter medication name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter medication ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter medication price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter initial stock: ");
                    int stock = scanner.nextInt();
                    pms.addMedication(name, id, price, stock);
                    break;
                case 2:
                    // Update stock of an existing medication
                    System.out.print("Enter medication ID to update stock: ");
                    String medId = scanner.nextLine();
                    System.out.print("Enter amount to update stock (use negative for reduction): ");
                    int amount = scanner.nextInt();
                    pms.updateStock(medId, amount);
                    break;
                case 3:
                    // Display all medications
                    pms.displayMedications();
                    break;
                case 4:
                    // Find a medication by ID
                    System.out.print("Enter medication ID to find: ");
                    String searchId = scanner.nextLine();
                    pms.findMedication(searchId);
                    break;
                case 5:
                    // Order medication
                    System.out.print("Enter medication ID to order: ");
                    String orderId = scanner.nextLine();
                    System.out.print("Enter quantity to order: ");
                    int quantity = scanner.nextInt();
                    pms.orderMedication(orderId, quantity, scanner);
                    break;
                case 6:
                    // Display orders placed
                    pms.displayOrders();
                    break;
                case 7:
                    // Cancel an order
                    pms.cancelOrder(scanner);
                    break;
                case 8:
                    // Exit the program
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}