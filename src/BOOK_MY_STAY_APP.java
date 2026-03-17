import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 2);
        roomAvailability.put("Double", 1);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}

class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save data
    public void save(BookingHistory history, RoomInventory inventory) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(history);
            oos.writeObject(inventory);

            System.out.println("Data saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }


    public Object[] load() {

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            BookingHistory history = (BookingHistory) ois.readObject();
            RoomInventory inventory = (RoomInventory) ois.readObject();

            System.out.println("Data loaded successfully!");

            return new Object[]{history, inventory};

        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh...");
            return null;
        }
    }
}

public class BOOK_MY_STAY_APP {

    public static void main(String[] args) {

        System.out.println("Data Persistence & Recovery");

        PersistenceService service = new PersistenceService();

        BookingHistory history;
        RoomInventory inventory;

        Object[] data = service.load();

        if (data != null) {
            history = (BookingHistory) data[0];
            inventory = (RoomInventory) data[1];
        } else {
            history = new BookingHistory();
            inventory = new RoomInventory();
        }

        Reservation r1 = new Reservation("Abhi", "Single");
        history.addReservation(r1);

        System.out.println("\nCurrent Bookings:");
        for (Reservation r : history.getReservations()) {
            System.out.println(r.getGuestName() + " - " + r.getRoomType());
        }


        service.save(history, inventory);
    }
}