package com.insane.cratesx.commands;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.CrateItem;
import com.insane.cratesx.library.XManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandSpin {

    boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Sorry, but console is not allowed.");
            return true;
        }

        Player player = (Player)commandSender;

        if (!player.hasPermission("cratesx.spin")){
            player.sendMessage(CratesX.PREFIX + "You don't have permission.");
            return true;
        }

        if (args.length != 4){
            player.sendMessage(CratesX.PREFIX + "Usage: /crate spin <CrateID> <Player> <ItemID>");
            return true;
        }

        Crate crate = XManager.getInstance().getCrate(args[1]);

        if (crate == null){
            player.sendMessage(CratesX.PREFIX + "Crate not found.");
            return true;
        }

        Player pl = Bukkit.getPlayer(args[2]);

        if (pl == null){
            player.sendMessage(CratesX.PREFIX + "Player not found.");
            return true;
        }

        CrateItem item = crate.getItem(args[3]);

        if (item == null){
            player.sendMessage(CratesX.PREFIX + "Item not found.");
            return true;
        }

        XManager.getInstance().setTwist(pl, item);
        player.sendMessage(CratesX.PREFIX + "Player §a" + pl.getName() + " §7will get " + item.getItem().getItemMeta().getDisplayName() + " §7next.");
        return false;
    }
}
