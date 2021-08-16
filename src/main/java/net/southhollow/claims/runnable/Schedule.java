package net.southhollow.claims.runnable;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.southhollow.claims.handler.tp.StorageHandler;
import net.southhollow.claims.storage.DataStorage;
import org.bukkit.scheduler.BukkitRunnable;

public class Schedule extends BukkitRunnable {

    @Override
    public void run() {
        final DataStorage data = DataStorage.getConfig();
        final StorageHandler storage = new StorageHandler();

        data.save();

        if(data.contains("claim-data")) {
            if(!data.getConfigurationSection("claim-data").getKeys(false).isEmpty()) {
                for(final String claimId : data.getConfigurationSection("claim-data").getKeys(false)) {
                    if(GriefPrevention.instance.dataStore.getClaim(Long.parseLong(claimId)) == null) {
                        storage.delete(claimId);
                    }
                }
            }
        }
    }
}
