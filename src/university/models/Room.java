package university.models;

import java.io.Serializable;
import university.enums.RoomType;

public class Room implements Serializable {
    private final String id;
    private final RoomType type;
    private final int capacity;

    public Room(String id, RoomType type, int capacity) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public RoomType getType() { return type; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return String.format("Room %s (%s, cap: %d)", id, type, capacity);
    }
}
