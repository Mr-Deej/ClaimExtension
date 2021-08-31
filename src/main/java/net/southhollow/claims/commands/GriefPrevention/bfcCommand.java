package net.southhollow.claims.commands.GriefPrevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.southhollow.claims.config.ClaimData;
import net.southhollow.claims.config.Config;
import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.MessageHandler;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bfcCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            MessageHandler.sendConsole("&cThis command can only be used in-game.");
            return true;
        }

        final Player player = (Player) sender;
        final Location loc = player.getLocation();
        final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);

        if(args.length == 0) {
            MessageHandler.sendMessage(player, Messages.NO_ARGUMENTS);
            return true;
        }

        if(claim == null) {
            MessageHandler.sendMessage(player, Messages.OUTSIDE_CLAIM);
            return true;
        }

        @SuppressWarnings("deprecation")
        final OfflinePlayer bannedPlayer = Bukkit.getOfflinePlayer(args[0]);
        final String accessDenied = claim.allowGrantPermission(player);
        boolean allowBan = false;

        if(accessDenied == null) { allowBan = true; }
        if(player.hasPermission("bfc.admin")) { allowBan = true; }

        if(!bannedPlayer.hasPlayedBefore()) {
            MessageHandler.sendMessage(player, Messages.placeholders(Messages.UNVALID_PLAYERNAME, args[0], player.getDisplayName(), null));
            return true;
        } else if(bannedPlayer == player) {
            MessageHandler.sendMessage(player, Messages.BAN_SELF);
            return true;
        } else if(bannedPlayer.getName().equals(claim.getOwnerName())) {
            MessageHandler.sendMessage(player, Messages.BAN_OWNER);
            return true;
        }

        if(bannedPlayer.isOnline()) {
            if(bannedPlayer.getPlayer().hasPermission("bfc.bypass")) {
                MessageHandler.sendMessage(player, Messages.placeholders(Messages.PROTECTED, bannedPlayer.getPlayer().getDisplayName(), null, null));
                return true;
            }
        }

        if(!allowBan) {
            MessageHandler.sendMessage(player, Messages.NO_ACCESS);
            return true;
        } else {
            final String claimOwner = claim.getOwnerName();

            if(setClaimData(player, claim.getID().toString(), bannedPlayer.getUniqueId().toString(), true)) {
                if(bannedPlayer.isOnline()) {
                    if(GriefPrevention.instance.dataStore.getClaimAt(bannedPlayer.getPlayer().getLocation(), true, claim) != null) {
                        if(GriefPrevention.instance.dataStore.getClaimAt(bannedPlayer.getPlayer().getLocation(), true, claim) == claim) {
                            final World world = claim.getGreaterBoundaryCorner().getWorld();
                            final int x = claim.getGreaterBoundaryCorner().getBlockX();
                            final int z = claim.getGreaterBoundaryCorner().getBlockZ();
                            final int y = world.getHighestBlockAt(x, z).getY();
                            final Location tpLoc = new Location(world, x, y, z).add(0D, 1D, 0D);

                            if(tpLoc.getBlock().getType().equals(Material.AIR)) {
                                if(Config.SAFE_LOCATION != null) {
                                    bannedPlayer.getPlayer().teleport(Config.SAFE_LOCATION);
                                } else { bannedPlayer.getPlayer().teleport(tpLoc.add(0D, 1D, 0D)); }
                            } else { bannedPlayer.getPlayer().teleport(tpLoc.add(0D, 1D, 0D)); }
                        }
                    }
                    MessageHandler.sendMessage(bannedPlayer.getPlayer(), Messages.placeholders(Messages.BANNED_TARGET, bannedPlayer.getName(), player.getDisplayName(), claimOwner));
                }

                MessageHandler.sendMessage(player, Messages.placeholders(Messages.BANNED, bannedPlayer.getName(), null, null));

            } else {
                MessageHandler.sendMessage(player, Messages.ALREADY_BANNED);
            }
        }
        return true;
    }

    private boolean setClaimData(Player player, String claimID, String bannedUUID, boolean add) {
        final ClaimData claimData = new ClaimData();

        return claimData.setClaimData(player, claimID, bannedUUID, add);
    }

}