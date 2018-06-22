package com.insane.cratesx.commands;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.XManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandGive {

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

        if (args.length != 4){
            player.sendMessage("§cUsage: /crate give <ID> <Player> <Count>");
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

        int count = XManager.getInstance().getCount(pl, crate);

        try{
            count += Integer.parseInt(args[3]);
        }
        catch(Exception ignored){
            player.sendMessage(CratesX.PREFIX + "Illegal count.");
            return true;
        }

        if (count <= 0){
            player.sendMessage(CratesX.PREFIX + "Crates count must be positive.");
            return true;
        }

        XManager.getInstance().setCount(pl, crate, count);
        player.sendMessage(CratesX.PREFIX + "Player §a" + pl.getName() + " §7was given a §ax" + count + " " + crate.getId() + " §7crate.");
        return false;
    }
}
