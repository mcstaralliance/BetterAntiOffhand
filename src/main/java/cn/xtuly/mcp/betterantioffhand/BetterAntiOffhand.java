package cn.xtuly.mcp.betterantioffhand;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BetterAntiOffhand extends JavaPlugin {
    FileConfiguration config;
    List<Integer> list = null;

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onEnable() {
        Config.rootPath = getDataFolder().getPath();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        config = Config.load("BetterAntiOffhand");
        list = (List<Integer>) config.getList("banlist");
        if(list == null){
            list = new ArrayList<Integer>(Arrays.asList(4226,4227,4238));
            Config.save(config,"BetterAntiOffhand");
        }
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(list.contains(p.getInventory().getItemInMainHand().getTypeId())){
                        if (p.getInventory().getItemInOffHand().getType() != Material.AIR) {
                            ItemStack temp = p.getInventory().getItemInOffHand();
                            p.getInventory().setItemInOffHand(null);
                            BetterAntiOffhand.this.insert(p, temp);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 5, 5);
    }

    public void insert(Player p, ItemStack stack) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(stack);
        } else {
            p.getWorld().dropItem(p.getLocation(), stack);
        }
        p.updateInventory();
    }

}
