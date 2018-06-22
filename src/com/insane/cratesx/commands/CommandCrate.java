package com.insane.cratesx.commands;

import com.insane.cratesx.CratesX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCrate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0){
            commandSender.sendMessage(CratesX.PREFIX + "§7Running CratesX v1.0");
            return true;
        }

        switch(args[0].toLowerCase()){
            case "open":
                return new CommandOpen().onCommand(commandSender, args);
            case "give":
                return new CommandGive().onCommand(commandSender, args);
            case "spin":
                return new CommandSpin().onCommand(commandSender, args);
            case "set":
                return new CommandSet().onCommand(commandSender, args);
            case "reload":
                return new CommandReload().onCommand(commandSender, args);
        }
        return false;
    }
}
