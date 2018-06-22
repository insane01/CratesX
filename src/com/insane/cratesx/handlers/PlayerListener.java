package com.insane.cratesx.handlers;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.XManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    // Disable clicks in crate menu
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (XManager.getInstance().isOpening((Player)event.getWhoClicked())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        XManager.getInstance().setOpening(event.getPlayer(), false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() != Material.ENDER_CHEST) return;

        // Installation of block for opening crate
        if (XManager.getInstance().getSetupList().containsKey(player)) {
            event.setCancelled(true);

            Crate crate = XManager.getInstance().getSetupList().get(player);
            XManager.getInstance().setCrateBlock(crate, block);
            XManager.getInstance().getSetupList().remove(player);
            return;
        }

        // Prohibition to break the crate
        Crate crate = XManager.getInstance().getCrate(event.getBlock());

        if (crate != null){
            event.setCancelled(true);
        }
    }

    // Opening crate by clicking on the chest
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock().getType() != Material.ENDER_CHEST) return;

        Player player = event.getPlayer();
        Crate crate = XManager.getInstance().getCrate(event.getClickedBlock());
        if (crate != null){
            event.setCancelled(true);
            int count = XManager.getInstance().getCount(player, crate);
            if (count == 0){
                player.sendMessage(CratesX.PREFIX + "You don't have crates.");
                return;
            }

            XManager.getInstance().setCount(player, crate, count-1);
            crate.open(player);
        }
    }
}
