package com.insane.cratesx.library;

import com.insane.cratesx.animations.AnimationType;
import com.insane.cratesx.animations.CrateAnimation;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Crate {

    private String id;
    private String name;
    private AnimationType animation;
    private ArrayList<CrateItem> items;

    private Effect effect = Effect.LAVA_POP;

    private ArrayList<CrateItem> tempList = new ArrayList<>();
    private Random rnd = new Random();

    Crate(String id, String name, AnimationType animation, ArrayList<CrateItem> items){
        this.id = id;
        this.name = name;
        this.animation = animation;
        this.items = items;

        // Add items to the temporary list, according to the chance.
        for (CrateItem item : items) {
            for (int i = 0; i < item.getChance(); i++) {
                tempList.add(item);
            }
        }
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public AnimationType getAnimationType(){
        return animation;
    }

    public Effect getEffect(){
        return effect;
    }

    public void setEffect(Effect e){
        this.effect = e;
    }

    public void open(Player player){
        XManager.getInstance().setOpening(player, true);

        // Set up animation
        CrateAnimation animation = new CrateAnimation(this, player);

        // Get the block of the chest, if it is registered.
        Block block = XManager.getInstance().getCrateBlock(this);
        if (block != null){
            if (block.getType() == Material.ENDER_CHEST)
                animation.setBlock(block);
        }

        animation.open();
    }

    public CrateItem getRandomItem(){
        Collections.shuffle(tempList);

        int Index = rnd.nextInt(tempList.size());
        return tempList.get(Index);
    }

    // Get item by id
    public CrateItem getItem(String s){
        for (CrateItem item : items){
            if (item.getItemId().equalsIgnoreCase(s))
                return item;
        }
        return null;
    }
}
