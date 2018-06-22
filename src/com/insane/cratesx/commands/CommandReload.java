package com.insane.cratesx.commands;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.CrateItem;
import com.insane.cratesx.library.XManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandReload {

    boolean onCommand(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("cratesx.reload")){
            commandSender.sendMessage(CratesX.PREFIX + "You don't have permission.");
            return true;
        }

        if (XManager.getInstance().isCratesOpening()){
            commandSender.sendMessage(CratesX.PREFIX + "You can't reload the plugin while crates are opening.");
            return true;
        }

        XManager.getInstance().reload();
        commandSender.sendMessage(CratesX.PREFIX + "Plugin reloaded.");
        return false;
    }
}
