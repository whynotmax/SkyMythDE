package de.skymyth.location.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Position {

    @Id
    String name;

    Location location;

    boolean warp; //Indicates if the position is a warp or not!

    /**
     * Converts a Bukkit location to a Position object
     *
     * @param location The Bukkit location
     * @return The Position object - Note that the name of this position object will be null - please set it manually!
     */
    public static Position fromBukkitLocation(Location location) {
        return new Position(null, location.clone(), false);
    }

    /**
     * Converts the Position object to a Bukkit location
     *
     * @return The Bukkit location
     */
    public Location toBukkitLocation() {
        return location;
    }

}
