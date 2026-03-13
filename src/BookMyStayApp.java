import java.util.*;

public class RoomAllocationService {

    // Track allocated room IDs for each room type
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Constructor
    public RoomAllocationService() {
        allocatedRooms.put("Single", new HashSet<>());
        allocatedRooms.put("Double", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());
    }

    public void allocateRoom(Queue<Reservation> requestQueue, RoomInventory inventory) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        while (!requestQueue.isEmpty()) {

            Reservation request = requestQueue.poll();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing request for: " + request.getGuestName());

            // Check availability
            if (availability.get(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness
                allocatedRooms.get(roomType).add(roomId);

                // Update inventory immediately
                availability.put(roomType, availability.get(roomType) - 1);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("No available rooms for type: " + roomType);
            }
        }
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        int number = allocatedRooms.get(roomType).size() + 1;
        return roomType.substring(0, 1).toUpperCase() + number;
    }
}
import java.util.LinkedList;
import java.util.Queue;

public class UseCase6RoomAllocation {

    public static void main(String[] args) {

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Booking Queue
        Queue<Reservation> requestQueue = new LinkedList<>();

        requestQueue.add(new Reservation("Alice", "Single"));
        requestQueue.add(new Reservation("Bob", "Double"));
        requestQueue.add(new Reservation("Charlie", "Single"));
        requestQueue.add(new Reservation("David", "Suite"));

        // Allocation Service
        RoomAllocationService allocator = new RoomAllocationService();

        allocator.allocateRoom(requestQueue, inventory);
    }
}
