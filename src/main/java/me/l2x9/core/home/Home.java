package me.l2x9.core.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.UUID;

@AllArgsConstructor
@Data
public class Home {
    private final String name;
    private final UUID owner;
    private Location location;
}
