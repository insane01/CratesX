package com.insane.cratesx.animations;

import com.insane.cratesx.library.Crate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Animation {

    private Player player;
    private Inventory inv;
    private Crate crate;

    Animation(Player player, Inventory inv, Crate crate){
        this.player = player;
        this.inv = inv;
        this.crate = crate;
    }

    public void animate(){}

    public Player getPlayer(){
        return player;
    }

    Inventory getInventory(){
        return inv;
    }

    public Crate getCrate(){
        return crate;
    }

    void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
