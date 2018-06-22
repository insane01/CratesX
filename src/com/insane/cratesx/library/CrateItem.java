package com.insane.cratesx.library;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateItem {

    private String crate;

    private String id;
    private ItemStack item;
    private ItemType type;
    private int chance;
    private boolean notify;

    private String command;
    private int money;

    CrateItem(String crate, String id, ItemStack item, ItemType type, int chance){
        this.crate = crate;
        this.id = id;
        this.item = item;
        this.type = type;
        this.chance = chance;
    }

    public String getCrateId(){
        return crate;
    }

    public String getItemId(){
        return id;
    }

    public ItemStack getItem(){
        return item;
    }

    int getChance(){
        return chance;
    }

    public boolean isNotify(){
        return notify;
    }

    void setNotify(boolean notify) {
        this.notify = notify;
    }

    void setCommand(String s){
        this.command = s;
    }

    void setMoney(int i){
        this.money = i;
    }

    // Give out a prize to the player
    public void give(Player player){
        switch(type){
            case ITEM:
                player.getInventory().addItem(item);
                break;
            case COMMAND:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{USERNAME}", player.getName()));
                break;
        }
        XManager.getInstance().setOpening(player, false);
    }
}
