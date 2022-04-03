package me.l2x9.core.impl.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Home {
    private final String name;
    private final UUID owner;
    private Location location;
}
