package com.insane.cratesx.commands;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.XManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandSet {

    boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Sorry, but console is not allowed.");
            return true;
        }

        Player player = (Player)commandSender;

        if (!player.hasPermission("cratesx.setup")){
            player.sendMessage(CratesX.PREFIX + "§cYou don't have permission.");
            return true;
        }

        if (args.length != 2){
            player.sendMessage("§cUsage: /crate set <ID>");
            return true;
        }

        Crate crate = XManager.getInstance().getCrate(args[1]);

        if (crate == null){
            player.sendMessage(CratesX.PREFIX + "Crate not found.");
            return true;
        }

        XManager.getInstance().getSetupList().put(player, crate);
        player.sendMessage(CratesX.PREFIX + "break block syka");
        return false;
    }
}
