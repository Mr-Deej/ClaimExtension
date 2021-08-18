package net.southhollow.claims;

import net.southhollow.claims.commands.ClaimTP.SetClaimName;
import net.southhollow.claims.commands.ClaimTP.SetClaimSpawn;
import net.southhollow.claims.commands.ClaimTP.TPListCommand;
import net.southhollow.claims.commands.ClaimTP.TeleportCommand;
import net.southhollow.claims.commands.GriefPrevention.bfcCommand;
import net.southhollow.claims.commands.GriefPrevention.bfcListCommand;
import net.southhollow.claims.commands.GriefPrevention.ubfcCommand;
import net.southhollow.claims.commands.SafeSpot;
import net.southhollow.claims.config.ClaimData;
import net.southhollow.claims.config.Config;
import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.CombatScheduler;
import net.southhollow.claims.handler.MessageHandler;
import net.southhollow.claims.listener.CombatMode;
import net.southhollow.claims.listener.GPBoatListener;
import net.southhollow.claims.listener.GPListener;
import net.southhollow.claims.listener.PlayerListener;
import net.southhollow.claims.runnable.Schedule;
import net.southhollow.claims.storage.DataStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public final class ClaimExtension extends JavaPlugin {

    public static ClaimExtension instance;

    private File dataFile;
    private FileConfiguration data;

    public static ClaimExtension getInstance() {
        return instance;
    }

    //Start up logic
    @Override
    public void onEnable() {
        instance = this;

        if(getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
            MessageHandler.sendConsole("&2[" + getDescription().getPrefix() + "] &7Successfully hooked into &eGriefPrevention");
            MessageHandler.sendConsole("");

            this.getServer().getPluginManager().registerEvents(new GPListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
            this.getCommand("banfromclaim").setExecutor(new bfcCommand());
            this.getCommand("unbanfromclaim").setExecutor(new ubfcCommand());
            //banfromclaimlist error see issue #3
            this.getCommand("banfromclaimlist").setExecutor(new bfcListCommand());
            this.getCommand("gpteleportlist").setExecutor(new TPListCommand());
            this.getCommand("gpteleport").setExecutor(new TeleportCommand());
            this.getCommand("setclaimspawn").setExecutor(new SetClaimSpawn());
            this.getCommand("setclaimname").setExecutor(new SetClaimName());

        } else {
            MessageHandler.sendConsole("&2[" + getDescription().getPrefix() + "] &cNo supported claim system was found.");
            MessageHandler.sendConsole("");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("bfcsafespot").setExecutor(new SafeSpot());

        createDatafile();
        new DataStorage();
        Messages.initialize();
        Config.initialize();
        ClaimData.createSection();
        new Schedule().runTaskTimer(this, 0L, (3600 * 20L));

        if(Config.COMBAT_ENABLED) {
            this.getServer().getPluginManager().registerEvents(new CombatMode(), this);
            new CombatScheduler().runTaskTimer(this, 0L, 20L);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ClaimData.cleanDatafile();
            }

        }.runTaskTimer(this, 30 * 20L, 3600 * 20L);
    }

    //shutdown logic
    @Override
    public void onDisable() {
        final DataStorage data = DataStorage.getConfig();
        data.save();
    }

    public FileConfiguration getDataFile() {
        return this.data;
    }

    public void createDatafile() {
        dataFile = new File(this.getDataFolder(), "data.dat");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
