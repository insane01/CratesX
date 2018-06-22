package com.insane.cratesx.library;

import com.insane.cratesx.animations.AnimationType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class XManager {

    private XManager() { }

    private static XManager instance = new XManager();

    public static XManager getInstance() {
        return instance;
    }

    private Plugin plugin;
    private ArrayList<Crate> crates = new ArrayList<>();
    private ArrayList<Player> opening = new ArrayList<>();

    private HashMap<Player, CrateItem> twist = new HashMap<>();
    private HashMap<Player, Crate> setupList = new HashMap<>();

    private HashMap<Location, Crate> crateBlocks = new HashMap<>();

    private File count, blocks;
    private FileConfiguration countCfg, blocksCfg;

    public void setup(Plugin plugin){
        this.plugin = plugin;

        File folder = new File(plugin.getDataFolder(), "crates");

        if (!folder.isDirectory()) {
            folder.mkdirs();
            plugin.saveResource("crates" + File.separator + "example.yml", false);
        }

        count = new File(plugin.getDataFolder(), "count.yml");
        countCfg = YamlConfiguration.loadConfiguration(count);

        blocks = new File(plugin.getDataFolder(), "blocks.yml");
        blocksCfg = YamlConfiguration.loadConfiguration(blocks);

        // Register all crates from folder
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (!fileEntry.getName().endsWith(".yml")) continue;
            Crate crate = initializeCrate(fileEntry.getName());

            if (crate == null){
                Bukkit.getConsoleSender().sendMessage("§c[CratesX] Can't register " + fileEntry.getName() + " crate.");
                continue;
            }

            crates.add(crate);

            Bukkit.getConsoleSender().sendMessage("[CratesX] Registered " + fileEntry.getName() + ".");
        }
    }

    public Plugin getPlugin(){
        return plugin;
    }

    public void reload(){
        crates.clear();
        opening.clear();
        twist.clear();
        setupList.clear();
        crateBlocks.clear();
        setup(plugin);
    }

    public boolean isCratesOpening(){
        return (opening.size() != 0);
    }

    private Crate initializeCrate(String s){
        File file = new File(plugin.getDataFolder(), "crates/" + s);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            return null;
        }

        String id = s.replace(".yml", "");
        String name = ChatColor.translateAlternateColorCodes('&', config.getString("settings.name"));
        AnimationType type = AnimationType.valueOf(config.getString("settings.animation").toUpperCase());

        ArrayList<CrateItem> items = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("items");
        for(int i = 0; i < section.getValues(false).keySet().size(); i++) {
            String itemId = section.getValues(false).keySet().toArray()[i].toString();
            CrateItem item = getCrateItem(id, config, itemId);

            if (item == null){
                return null;
            }

            items.add(item);
        }

        Crate crate = new Crate(id, name, type, items);
        initializeCrateBlock(crate);

        if (config.contains("settings.effect")){
            crate.setEffect(Effect.valueOf(config.getString("settings.effect").toUpperCase()));
        }

        return crate;
    }

    private void initializeCrateBlock(Crate crate){
        if (blocksCfg.contains("blocks." + crate.getId())){
            ConfigurationSection blockSection = blocksCfg.getConfigurationSection("blocks." + crate.getId());
            World world = Bukkit.getWorld(blockSection.getString("world"));

            if (world != null){
                int x = blockSection.getInt("x");
                int y = blockSection.getInt("y");
                int z = blockSection.getInt("z");
                crateBlocks.put(new Location(world,x,y,z), crate);
            }
        }
    }

    private CrateItem getCrateItem(String crate, FileConfiguration config, String itemId){
        ConfigurationSection section = config.getConfigurationSection("items." + itemId);

        String itemID = section.getString("item.id");
        ItemStack is = new ItemStack(Material.valueOf(itemID.toUpperCase()));
        ItemType type = ItemType.valueOf(section.getString("type").toUpperCase());

        int chance = section.getInt("chance");

        if (section.contains("item.count")){
            is = new ItemStack(Material.valueOf(itemID.toUpperCase()), section.getInt("item.count"));
        }

        if (section.contains("item.data")){
            is.setDurability((short)section.getInt("item.data"));
        }

        ItemMeta meta = is.getItemMeta();

        if (section.contains("name")){
            String itemName = section.getString("name");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        }

        if (section.contains("lore")){
            ArrayList<String> list = new ArrayList<>();
            for (String line : section.getStringList("lore")){
                list.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(list);
        }

        is.setItemMeta(meta);

        if (is.getType() == Material.SKULL_ITEM){
            if (section.contains("item.skull-owner")){
                SkullMeta skull_meta = ((SkullMeta) is.getItemMeta());
                skull_meta.setOwner(section.getString("item.skull-owner"));
                is.setItemMeta(skull_meta);
                is.setDurability((short)3);
            }
        }

        CrateItem item = new CrateItem(crate, itemId, is, type, chance);

        if (type == ItemType.COMMAND){
            if (!section.contains("command")){
                return null;
            }
            item.setCommand(section.getString("command"));
        }

        if (type == ItemType.MONEY){
            if (!section.contains("money")){
                return null;
            }
            item.setMoney(section.getInt("money"));
        }

        if (section.contains("notify")){
            item.setNotify(section.getBoolean("notify"));
        }

        return item;
    }

    public Crate getCrate(String id){
        for (Crate crate : crates){
            if (crate.getId().equalsIgnoreCase(id))
                return crate;
        }
        return null;
    }

    public Crate getCrate(Block block){
        return crateBlocks.get(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
    }

    Block getCrateBlock(Crate crate){
        for (Location loc : crateBlocks.keySet()){
            if (crateBlocks.get(loc).getId().equals(crate.getId()))
                return loc.getBlock();
        }
        return null;
    }

    // Methods for crates count
    public int getCount(Player player, Crate crate){
        if (countCfg.contains(player.getName() + "." + crate.getId())){
            return countCfg.getInt(player.getName() + "." + crate.getId());
        }
        else return 0;
    }

    public void setCount(Player player, Crate crate, int count){
        countCfg.set(player.getName() + "." + crate.getId(), count);
        save();
    }

    public boolean isOpening(Player player){
        return opening.contains(player);
    }

    public void setOpening(Player player, boolean b){
        if (b) opening.add(player);
        else opening.remove(player);
    }

    public void setTwist(Player player, CrateItem item){
        twist.put(player, item);
    }

    public CrateItem getTwistItem(Crate crate, Player player){
        if (!twist.containsKey(player)) return null;
        CrateItem item = twist.get(player);
        if (crate.getId().equalsIgnoreCase(item.getCrateId())){
            twist.remove(player);
            return item;
        }
        return null;
    }

    public void setCrateBlock(Crate crate, Block block){
        blocksCfg.set("blocks." + crate.getId() + ".world", block.getWorld().getName());
        blocksCfg.set("blocks." + crate.getId() + ".x", block.getX());
        blocksCfg.set("blocks." + crate.getId() + ".y", block.getY());
        blocksCfg.set("blocks." + crate.getId() + ".z", block.getZ());
        save();
        initializeCrateBlock(crate);
    }

    public HashMap<Player, Crate> getSetupList(){
        return setupList;
    }

    // Save count.yml and blocks.yml
    private void save(){
        try {
            countCfg.save(count);
            blocksCfg.save(blocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
