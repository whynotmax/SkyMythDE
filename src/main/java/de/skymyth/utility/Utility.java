package de.skymyth.utility;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@UtilityClass
@Getter
public class Utility {

    public static final Map<Player, Player> MESSAGE = new HashMap<>();
    public static final Cache<Player, Player> TELEPORT = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

}
