class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: $" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("-------------------------");
    }
}}

        import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 3);
        availability.put("Double", 2);
        availability.put("Suite", 0);
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }
}

import java.util.Map;

public class RoomSearchService {

    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Available Rooms:");
        System.out.println("================");

        // Single Room
        if (availability.get("Single") > 0) {
            singleRoom.displayRoomDetails();
        }

        // Double Room
        if (availability.get("Double") > 0) {
            doubleRoom.displayRoomDetails();
        }

        // Suite Room
        if (availability.get("Suite") > 0) {
            suiteRoom.displayRoomDetails();
        }
    }
}
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        // Create Room Objects
        Room singleRoom = new Room("Single", 100, "WiFi, TV, Single Bed");
        Room doubleRoom = new Room("Double", 180, "WiFi, TV, Double Bed");
        Room suiteRoom = new Room("Suite", 300, "WiFi, TV, King Bed, Mini Bar");

        // Create Inventory
        RoomInventory inventory = new RoomInventory();

        // Search Service
        RoomSearchService searchService = new RoomSearchService();

        // Perform Search
        searchService.searchAvailableRooms(
                inventory,
                singleRoom,
                doubleRoom,
                suiteRoom);
    }
}
