package ftp.sh.core.home;

import org.bukkit.Location;

import java.util.UUID;

public class Home {

    private final String name;
    private final UUID owner;
    private Location location;

    public Home(String name, Location location, UUID owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getOwner() {
        return owner;
    }
}
