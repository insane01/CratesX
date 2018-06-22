package com.insane.cratesx.animations;

import com.insane.cratesx.CratesX;
import com.insane.cratesx.library.Crate;
import com.insane.cratesx.library.CrateItem;
import com.insane.cratesx.library.XManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class CrateAnimation {

    private Crate crate;
    private Player player;
    private Inventory inv;

    private Block block;
    private Random rnd = new Random();

    public CrateAnimation(Crate crate, Player player){
        this.crate = crate;
        this.player = player;
        inv = Bukkit.createInventory(null, 27, crate.getName());
    }

    public void setBlock(Block b){
        this.block = b;
    }

    public void open(){
        player.openInventory(inv);

        Animation animation = null;

        switch (crate.getAnimationType()){
            case SIMPLE:
                animation = new SimpleAnimation(player, inv, crate);
                break;
            case RANDOM:
                animation = new RandomAnimation(player, inv, crate);
                break;
        }

        animation.animate();

        CrateItem item = XManager.getInstance().getTwistItem(crate, player);

        if (item != null){
            spin(item);
            return;
        }

        spin(crate.getRandomItem());
    }

    private void spin(CrateItem item){
        new BukkitRunnable(){
            int ticks = 0;

            public void run(){
                ticks++;

                if (block != null){
                    block.getWorld().playEffect(getRandomLocation(block.getLocation()), crate.getEffect(), 10);
                }

                if (!player.getInventory().getName().equals(inv.getName())){
                    player.openInventory(inv);
                }

                if (ticks % 2 == 0 && ticks < 264){
                    changeGlassColor();
                }

                if (ticks % 4 == 0 && ticks > 264){
                    changeGlassColor(getRandomGlassPane());
                }

                if (ticks < 25) return;

                if (ticks < 100 || (ticks % 2 == 0 && ticks < 150) || (ticks % 4 == 0 && ticks > 150 && ticks < 200) || (ticks % 8 == 0 && ticks > 200 && ticks < 250)){
                    for (int i = 9; i < 17; i++){
                        inv.setItem(i, inv.getItem(i+1));
                    }

                    inv.setItem(17, ticks != 216 ? crate.getRandomItem().getItem() : item.getItem());
                }

                if (ticks == 264){
                    for (int i = 9; i <= 17; i++){
                        if (i == 13) continue;
                        inv.setItem(i, new ItemStack(Material.AIR));
                    }
                    if (item.isNotify()) {
                        for (Player player : Bukkit.getOnlinePlayers()){
                            player.sendMessage(CratesX.PREFIX + "Player §a" + player.getName() + " §7opened " + crate.getName() + " §7and got " + item.getItem().getItemMeta().getDisplayName() + "§7.");
                        }
                    }
                }

                if (ticks == 300){
                    cancel();
                    item.give(player);
                    player.closeInventory();
                }
            }
        }.runTaskTimer(XManager.getInstance().getPlugin(), 1L, 1L);
    }

    private ItemStack getRandomGlassPane(){
        short[] arr = new short[] { 1,2,3,4,5,6,9,10,11,13,14};
        short data = arr[rnd.nextInt(arr.length)];
        return new ItemStack(Material.STAINED_GLASS_PANE, 1, data);
    }

    private void changeGlassColor(ItemStack item){
        for (int i = 0; i <= 8; i++){
            inv.setItem(i, item);
        }
        for (int i = 18; i <= 26; i++){
            inv.setItem(i, item);
        }
        player.updateInventory();
    }

    private void changeGlassColor(){
        for (int i = 0; i <= 8; i++){
            inv.setItem(i, getRandomGlassPane());
        }
        for (int i = 18; i <= 26; i++){
            inv.setItem(i, getRandomGlassPane());
        }
        player.updateInventory();
    }

    private Location getRandomLocation(Location loc){
        loc.add(rnd.nextDouble(), 1, rnd.nextDouble());
        return loc;
    }
}
