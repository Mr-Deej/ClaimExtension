package net.southhollow.claims.listener;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.southhollow.claims.ClaimExtension;
import net.southhollow.claims.config.ClaimData;
import net.southhollow.claims.config.Config;
import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.MessageHandler;
import net.southhollow.claims.handler.ParticleHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class GPListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerEnterClaim(PlayerMoveEvent e) {
        Location locFrom = e.getFrom();
        Location locTo = e.getTo();

        if (locFrom.getBlock().equals(locTo.getBlock())) {
            return;
        }

        Player player = e.getPlayer();
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(locTo, true, null);
        ParticleHandler particleHandler = new ParticleHandler(e.getTo());

        if (player.hasPermission("bfc.bypass")) {
            return;
        }

        if (claim != null) {
            UUID ownerUUID = claim.ownerID;
            String claimID = claim.getID().toString();
            boolean hasAttacked = false;

            if(CombatMode.ATTACKER.containsKey(player.getUniqueId()))
                hasAttacked = CombatMode.ATTACKER.get(player.getUniqueId()).equals(ownerUUID);

            if (playerBanned(player, claim, claimID) && !hasAttacked) {
                if (claim.contains(locFrom, true, false)) {
                    if (playerBanned(player, claim, claimID)) {
                        World world = claim.getGreaterBoundaryCorner().getWorld();
                        int x = claim.getGreaterBoundaryCorner().getBlockX();
                        int z = claim.getGreaterBoundaryCorner().getBlockZ();
                        int y = world.getHighestBlockAt(x, z).getY();
                        Location tpLoc = new Location(world, x, y, z).add(0D, 1D, 0D);

                        if(tpLoc.getBlock().getType().equals(Material.AIR)) {
                            if(Config.SAFE_LOCATION != null) {
                                player.teleport(Config.SAFE_LOCATION);
                            } else { player.teleport(tpLoc.add(0D, 1D, 0D)); }
                        } else { player.teleport(tpLoc.add(0D, 1D, 0D)); }

                    } else {
                        final Location tpLoc = player.getLocation().add(e.getFrom().toVector().subtract(e.getTo().toVector()).normalize().multiply(3));

                        if(tpLoc.getBlock().getType().equals(Material.AIR)) {
                            player.teleport(tpLoc);
                        }
                        else {
                            final Location safeLoc = tpLoc.getWorld().getHighestBlockAt(tpLoc).getLocation().add(0D, 1D, 0D);
                            player.teleport(safeLoc);
                        }

                        if(e.getTo().getBlockX() == e.getFrom().getBlockX()) { particleHandler.drawCircle(1, true); }
                        else { particleHandler.drawCircle(1, false); }
                    }

                } else {
                    final Location tpLoc = player.getLocation().add(e.getFrom().toVector().subtract(e.getTo().toVector()).normalize().multiply(3));
                    if(tpLoc.getBlock().getType().equals(Material.AIR)) { player.teleport(tpLoc); }
                    else {
                        final Location safeLoc = tpLoc.getWorld().getHighestBlockAt(tpLoc).getLocation().add(0D, 1D, 0D);
                        player.teleport(safeLoc);
                    }

                    if(e.getTo().getBlockX() == e.getFrom().getBlockX()) { particleHandler.drawCircle(1, true); }
                    else { particleHandler.drawCircle(1, false); }
                }

                if(!MessageHandler.spamMessageClaim.contains(player.getUniqueId().toString())) {
                    MessageHandler.sendTitle(player, Messages.TITLE_MESSAGE, Messages.SUBTITLE_MESSAGE);
                    MessageHandler.spamMessageClaim.add(player.getUniqueId().toString());

                    Bukkit.getScheduler().runTaskLater(ClaimExtension.getInstance(), () -> {
                        MessageHandler.spamMessageClaim.remove(player.getUniqueId().toString());
                    }, 5L * 20L);
                }
            }

        }

    }

    private boolean playerBanned(Player player, Claim claim, String claimID) {
        final ClaimData claimData = new ClaimData();
        if(claimData.checkClaim(claimID)) {
            if(claimData.bannedPlayers(claimID) != null) {
                for(final String bp : claimData.bannedPlayers(claimID)) {
                    if(bp.equals(player.getUniqueId().toString())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
