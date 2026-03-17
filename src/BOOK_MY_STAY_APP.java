import java.util.*;


class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}


class Reservation {
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


class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 1);
        roomAvailability.put("Double", 1);
        roomAvailability.put("Suite", 1);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}


class RoomAllocationService {

    public void allocateRoom(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        String type = reservation.getRoomType();


        if (!inventory.getRoomAvailability().containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }

        int available = inventory.getRoomAvailability().get(type);


        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + type);
        }


        String roomId = type + "-" + available;


        inventory.updateAvailability(type, available - 1);

        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }
}

public class BOOK_MY_STAY_APP {

    public static void main(String[] args) {

        System.out.println("Booking with Validation");

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();


        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("John", "Luxury"); // ❌ invalid

        try {
            service.allocateRoom(r1, inventory);
            service.allocateRoom(r2, inventory); // will throw error
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("System continues safely...");
    }
}