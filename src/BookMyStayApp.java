import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Room inventory
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return rooms.containsKey(roomType) && rooms.get(roomType) > 0;
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int available = rooms.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
        rooms.put(roomType, available - 1);
    }
}

// Validator
class ReservationValidator {
    public void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {
        if (guestName == null || guestName.isBlank()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!inventory.isAvailable(roomType)) {
            throw new InvalidBookingException("Room type unavailable or invalid: " + roomType);
        }
    }
}

// Main runner class (must match filename!)
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();

        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Deluxe/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input
            validator.validate(guestName, roomType, inventory);

            // Allocate room
            inventory.allocateRoom(roomType);
            System.out.println("Booking successful for " + guestName + " in " + roomType);

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
