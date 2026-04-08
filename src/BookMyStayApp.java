import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
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

    public void restoreRoom(String roomType) {
        rooms.put(roomType, rooms.getOrDefault(roomType, 0) + 1);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }
}

class CancellationService {
    private Stack<String> releasedRoomIds = new Stack<>();
    private Map<String, String> reservationRoomTypeMap = new HashMap<>();

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) throws InvalidBookingException {
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            throw new InvalidBookingException("Reservation not found or already cancelled: " + reservationId);
        }
        String roomType = reservationRoomTypeMap.remove(reservationId);
        releasedRoomIds.push(reservationId);
        inventory.restoreRoom(roomType);
        System.out.println("Cancelled reservation " + reservationId + " for room type " + roomType);
    }

    public void showRollbackHistory() {
        System.out.println("=== Rollback History (most recent first) ===");
        for (String resId : releasedRoomIds) {
            System.out.println("Cancelled Reservation ID: " + resId);
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        try {
            // Simulate bookings
            inventory.allocateRoom("Deluxe");
            cancellationService.registerBooking("R101", "Deluxe");

            inventory.allocateRoom("Suite");
            cancellationService.registerBooking("R102", "Suite");

            // Cancel one booking
            cancellationService.cancelBooking("R101", inventory);

            // Show rollback history
            cancellationService.showRollbackHistory();

            // Display inventory after rollback
            System.out.println("=== Current Inventory ===");
            for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (InvalidBookingException e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
    }
}
