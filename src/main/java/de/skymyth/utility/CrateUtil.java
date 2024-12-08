package de.skymyth.utility;

import lombok.experimental.UtilityClass;
import net.raynight.crates.utils.Util;
import net.raynight.crates.utils.crate.Crate;
import net.raynight.crates.utils.crate.CrateManager;
import org.bukkit.entity.Player;

@UtilityClass
public class CrateUtil {

    public void giveCrate(Player target, String crateName, int amount) {
        Crate crate = CrateManager.fromName(crateName);
        if (crate == null) {
            return;
        }
        Util.giveItem(target, crate.getAsItem(), amount);
    }

}
