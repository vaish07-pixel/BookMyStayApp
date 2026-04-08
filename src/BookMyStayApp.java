import java.io.*;
import java.util.*;

// Reservation class
class Reservation {
    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getId() { return id; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return id + "," + guestName + "," + roomType;
    }

    public static Reservation fromString(String line) {
        String[] parts = line.split(",");
        return new Reservation(parts[0], parts[1], parts[2]);
    }
}

// Booking history
class BookingHistory {
    private List<Reservation> confirmedReservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// Room inventory
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        if (!rooms.containsKey(roomType)) return false;
        int available = rooms.get(roomType);
        if (available <= 0) return false;
        rooms.put(roomType, available - 1);
        return true;
    }

    public void restoreRoom(String roomType) {
        rooms.put(roomType, rooms.getOrDefault(roomType, 0) + 1);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public static RoomInventory fromFile(String filePath) {
        RoomInventory inventory = new RoomInventory();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            Map<String, Integer> restored = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                restored.put(parts[0], Integer.parseInt(parts[1]));
            }
            inventory.rooms = restored;
        } catch (IOException e) {
            System.out.println("No inventory file found. Starting fresh.");
        }
        return inventory;
    }
}

// Persistence service
class FilePersistenceService {
    public void saveInventory(RoomInventory inventory, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(inventory.toString());
            System.out.println("Inventory state saved.");
        } catch (IOException e) {
            System.out.println("Failed to save inventory: " + e.getMessage());
        }
    }

    public void saveHistory(BookingHistory history, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Reservation r : history.getConfirmedReservations()) {
                writer.write(r.toString() + "\n");
            }
            System.out.println("Booking history saved.");
        } catch (IOException e) {
            System.out.println("Failed to save history: " + e.getMessage());
        }
    }

    public BookingHistory loadHistory(String filePath) {
        BookingHistory history = new BookingHistory();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                history.addReservation(Reservation.fromString(line));
            }
            System.out.println("Booking history restored.");
        } catch (IOException e) {
            System.out.println("No history file found. Starting fresh.");
        }
        return history;
    }
}

// Main runner
public class BookMyStayApp {
    public static void main(String[] args) {
        FilePersistenceService persistence = new FilePersistenceService();

        // Restore state
        RoomInventory inventory = RoomInventory.fromFile("inventory.txt");
        BookingHistory history = persistence.loadHistory("history.txt");

        // Simulate new booking
        Reservation r1 = new Reservation("R" + System.currentTimeMillis(), "Alice", "Deluxe");
        if (inventory.allocateRoom(r1.getRoomType())) {
            history.addReservation(r1);
            System.out.println("Booking successful: " + r1);
        } else {
            System.out.println("Booking failed: No availability for " + r1.getRoomType());
        }

        // Show current state
        System.out.println("=== Current Reservations ===");
        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println(r);
        }

        System.out.println("=== Current Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Save state before shutdown
        persistence.saveInventory(inventory, "inventory.txt");
        persistence.saveHistory(history, "history.txt");
    }
}
