import java.util.*;
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

    public void increaseAvailability(String roomType) {
        roomAvailability.put(roomType,
                roomAvailability.getOrDefault(roomType, 0) + 1);
    }

    public void decreaseAvailability(String roomType) {
        roomAvailability.put(roomType,
                roomAvailability.getOrDefault(roomType, 0) - 1);
    }
}


class RoomAllocationService {

    private Map<Reservation, String> allocatedRooms;

    public RoomAllocationService() {
        allocatedRooms = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();
        int available = inventory.getRoomAvailability().getOrDefault(type, 0);

        if (available <= 0) {
            System.out.println("No rooms available for " + type);
            return;
        }

        String roomId = type + "-" + available;

        allocatedRooms.put(reservation, roomId);
        inventory.decreaseAvailability(type);

        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }

    public String getAllocatedRoom(Reservation reservation) {
        return allocatedRooms.get(reservation);
    }

    public void removeAllocation(Reservation reservation) {
        allocatedRooms.remove(reservation);
    }
}


class CancellationService {

    private Stack<String> rollbackStack;

    public CancellationService() {
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(Reservation reservation,
                              RoomAllocationService allocator,
                              RoomInventory inventory) {

        String roomId = allocator.getAllocatedRoom(reservation);

        // ❗ Validation
        if (roomId == null) {
            System.out.println("Invalid cancellation: No booking found");
            return;
        }

        rollbackStack.push(roomId);


        String roomType = reservation.getRoomType();

        inventory.increaseAvailability(roomType);

        allocator.removeAllocation(reservation);

        System.out.println("Booking cancelled for Guest: "
                + reservation.getGuestName()
                + ", Released Room ID: " + roomId);
    }
}


public class BOOK_MY_STAY_APP {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation & Rollback");

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocator = new RoomAllocationService();
        CancellationService canceller = new CancellationService();


        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");

        allocator.allocateRoom(r1, inventory);
        allocator.allocateRoom(r2, inventory);


        canceller.cancelBooking(r2, allocator, inventory);
    }
}