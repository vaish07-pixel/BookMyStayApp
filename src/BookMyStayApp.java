import java.util.*;

// Represents a reservation request
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
}

// Shared booking request queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        synchronized (this) {
            queue.add(reservation);
        }
    }

    public Reservation getRequest() {
        synchronized (this) {
            return queue.poll();
        }
    }
}

// Room inventory
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("Deluxe", 2);
        rooms.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        if (!rooms.containsKey(roomType)) return false;
        int available = rooms.get(roomType);
        if (available <= 0) return false;
        rooms.put(roomType, available - 1);
        return true;
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }
}

// Booking processor (runs in threads)
class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;

    public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation;
            synchronized (bookingQueue) {
                reservation = bookingQueue.getRequest();
            }
            if (reservation == null) break; // no more requests

            synchronized (inventory) {
                boolean success = inventory.allocateRoom(reservation.getRoomType());
                if (success) {
                    System.out.println("Booking successful: " + reservation.getGuestName() +
                            " -> " + reservation.getRoomType());
                } else {
                    System.out.println("Booking failed (no availability): " + reservation.getGuestName() +
                            " -> " + reservation.getRoomType());
                }
            }
        }
    }
}

// Main runner
public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple guests submitting requests
        bookingQueue.addRequest(new Reservation("R101", "Alice", "Deluxe"));
        bookingQueue.addRequest(new Reservation("R102", "Bob", "Suite"));
        bookingQueue.addRequest(new Reservation("R103", "Charlie", "Deluxe"));
        bookingQueue.addRequest(new Reservation("R104", "Diana", "Suite")); // will fail (only 1 Suite)

        // Create booking processor threads
        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory));

        // Start concurrent processing
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Show final inventory state
        System.out.println("=== Final Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.getRooms().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
