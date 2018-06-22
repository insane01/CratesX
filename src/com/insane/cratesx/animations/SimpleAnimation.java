package com.insane.cratesx.animations;

import com.insane.cratesx.library.Crate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SimpleAnimation extends Animation {

    SimpleAnimation(Player player, Inventory inv, Crate crate) {
        super(player, inv, crate);
    }

    @Override
    public void animate(){
        ItemStack mainItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
        for (int i = 0; i < 27; i++) {
            this.getInventory().setItem(i, mainItem);
            this.getPlayer().updateInventory();
            sleep(20);
        }

        for (int i = 9; i <= 17; i++){
            this.getInventory().setItem(i, this.getCrate().getRandomItem().getItem());
        }

        this.getPlayer().updateInventory();
    }
}
