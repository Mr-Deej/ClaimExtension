package net.southhollow.claims.handler.tp;

import net.southhollow.claims.storage.DataStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class StorageHandler {

    final DataStorage data = DataStorage.getConfig();

    public StorageHandler() {

    }

    public boolean stored(String claimId) {
        if(data.contains("claim-data." + claimId)) { return true; }

        return false;
    }

    public boolean storedLocation(String claimId) {
        if(data.contains("claim-data." + claimId + ".X")) { return true; }

        return false;
    }

    public boolean storedName(String claimId) {
        if(data.contains("claim-data." + claimId + ".name")) { return true; }

        return false;
    }

    public void setLocation(String claimId, Location loc) {
        final String path = "claim-data." + claimId;

        final double locX = loc.getX();
        final double locY = loc.getY();
        final double locZ = loc.getZ();
        final double locYaw = loc.getYaw();
        final String world = loc.getWorld().getName();

        data.set(path + ".X", locX);
        data.set(path + ".Y", locY);
        data.set(path + ".Z", locZ);
        data.set(path + ".yaw", locYaw);
        data.set(path + ".world", world);
    }

    public void setName(String claimId, String name) {
        final String path = "claim-data." + claimId + ".name";
        data.set(path, name);
    }

    public String getName(String claimId) {
        final String path = "claim-data." + claimId + ".name";

        if(data.contains(path)) { return data.getString(path); }

        return null;
    }

    public Location getLocation(String claimId) {
        final String path = "claim-data." + claimId;

        if(data.contains(path)) {
            final double locX = data.getDouble(path + ".X");
            final double locY = data.getDouble(path + ".Y");
            final double locZ = data.getDouble(path + ".Z");
            final float locYaw = (float) data.getDouble(path + ".yaw");
            final World world = Bukkit.getWorld(data.getString(path + ".world"));

            final Location loc = new Location(world, locX, locY, locZ,locYaw, 0.0F);

            return loc;
        }

        return null;
    }

    public void delete(String claimId) {
        final String path = "claim-data." + claimId;

        if(stored(claimId)) { data.set(path, null); }
    }
}
