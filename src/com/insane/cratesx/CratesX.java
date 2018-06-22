package com.insane.cratesx;

import com.insane.cratesx.commands.CommandCrate;
import com.insane.cratesx.handlers.PlayerListener;
import com.insane.cratesx.library.XManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CratesX extends JavaPlugin implements Listener {

    public static String PREFIX = "§aCratesX §7>> ";

    @Override
    public void onEnable(){
        if (System.getProperty("CratesXLoaded") != null){
            Bukkit.getConsoleSender().sendMessage("§c[CratesX] Please, don't use /reload command.");
        }

        System.setProperty("CratesXLoaded", "true");

        XManager.getInstance().setup(this);
        getCommand("crate").setExecutor(new CommandCrate());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }
}
