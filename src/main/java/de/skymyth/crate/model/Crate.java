package de.skymyth.crate.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class Crate {

    @Id
    String name;
    String displayName;
    ItemStack displayItem;
    List<ItemStack> itemStacks;
    boolean enabled;


}
