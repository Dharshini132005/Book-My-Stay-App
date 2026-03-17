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
    }
    public synchronized boolean allocateRoom(String roomType) {

        int available = roomAvailability.getOrDefault(roomType, 0);

        if (available <= 0) {
            return false;
        }

        roomAvailability.put(roomType, available - 1);
        return true;
    }
}

class BookingTask extends Thread {

    private Reservation reservation;
    private RoomInventory inventory;

    public BookingTask(Reservation reservation, RoomInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        boolean success = inventory.allocateRoom(reservation.getRoomType());

        if (success) {
            System.out.println("Booking SUCCESS for "
                    + reservation.getGuestName()
                    + " (" + reservation.getRoomType() + ")");
        } else {
            System.out.println("Booking FAILED for "
                    + reservation.getGuestName()
                    + " (" + reservation.getRoomType() + ")");
        }
    }
}

public class BOOK_MY_STAY_APP {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        RoomInventory inventory = new RoomInventory();


        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Single");

        BookingTask t1 = new BookingTask(r1, inventory);
        BookingTask t2 = new BookingTask(r2, inventory);

        t1.start();
        t2.start();
    }
}