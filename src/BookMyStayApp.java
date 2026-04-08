import java.util.*;

// Represents a reservation
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

// Maintains booking history
class BookingHistory {
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

// Generates reports from booking history
class BookingReportService {
    public void generateReport(BookingHistory history) {
        System.out.println("=== Booking Summary Report ===");
        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println("Reservation ID: " + r.getId() +
                    ", Guest: " + r.getGuestName() +
                    ", Room: " + r.getRoomType());
        }
        System.out.println("Total Bookings: " + history.getConfirmedReservations().size());
    }
}

// Main runner class (must be public and match filename)
public class BookMyStayApp {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        // Example reservations
        history.addReservation(new Reservation("R101", "Alice", "Deluxe"));
        history.addReservation(new Reservation("R102", "Bob", "Suite"));

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}
