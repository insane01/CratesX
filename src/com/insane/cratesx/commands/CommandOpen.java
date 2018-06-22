package com.insane.cratesx.commands;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.XManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandOpen {

    boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Sorry, but console is not allowed.");
            return true;
        }

        Player player = (Player)commandSender;

        if (!player.hasPermission("cratesx.open")){
            player.sendMessage(CratesX.PREFIX + "You don't have permission.");
            return true;
        }

        if (args.length != 2){
            player.sendMessage("§cUsage: /crate open <ID>");
            return true;
        }

        Crate crate = XManager.getInstance().getCrate(args[1]);

        if (crate == null){
            player.sendMessage(CratesX.PREFIX + "Crate not found.");
            return true;
        }

        if (XManager.getInstance().isOpening(player)){
            player.sendMessage(CratesX.PREFIX + "You are already opening crate.");
            return true;
        }

        int count = XManager.getInstance().getCount(player, crate);
        if (count == 0){
            player.sendMessage(CratesX.PREFIX + "You don't have crates.");
            return true;
        }

        XManager.getInstance().setCount(player, crate, count-1);
        crate.open(player);
        return false;
    }
}
