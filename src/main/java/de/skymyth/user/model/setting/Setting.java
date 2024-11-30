package de.skymyth.user.model.setting;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PUBLIC, makeFinal = true)
public enum Setting {

    SHOW_CLAN_IN_CHAT("Clan im Chat anzeigen", Material.NAME_TAG, 0, List.of(0, 1), List.of("§cDeaktiviert", "§aAktiviert")),
    ;

    String name;
    Material display;

    int defaultValue;
    List<Integer> availableValues;
    List<String> valueNames;

    public static final Setting[] VALUES = values();
    public static final Map<Setting, Integer> DEFAULT_SETTINGS = Map.of(
            SHOW_CLAN_IN_CHAT, 0
    );

}
