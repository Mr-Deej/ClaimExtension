package net.southhollow.claims.commands.ClaimTP;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.southhollow.claims.config.Config;
import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.MessageHandler;
import net.southhollow.claims.handler.tp.Cooldown;
import net.southhollow.claims.handler.tp.StorageHandler;
import net.southhollow.claims.handler.tp.Warmup;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportCommand implements CommandExecutor {

    private long claimId = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            MessageHandler.sendConsole("&cThis command can only be used in-game.");
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        Claim claim = null;
        final StorageHandler storage = new StorageHandler();
        final Cooldown cooldown = new Cooldown();
        final Warmup warmup = new Warmup();

        if(args.length != 0) {
            if(isNumber(args[0])) {
                this.claimId = Long.parseLong(args[0]);
            } else {
                MessageHandler.sendMessage(player, Messages.UNVALID_NUMBER);
                return true;
            }
        } else {
            MessageHandler.sendMessage(player, Messages.UNVALID_CLAIMID);
            return true;
        }

        for(int i = 0; i < GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().toArray().length; i++) {
            final Claim claims = (Claim) GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().toArray()[i];

            if(claims.getID() == this.claimId) { claim = claims; }
        }

        if(claim == null) {
            MessageHandler.sendMessage(player, Messages.UNVALID_CLAIMID);
            return true;
        }

        final Location locMin = claim.getLesserBoundaryCorner();
        final Location locMax = claim.getGreaterBoundaryCorner();

        final double locX = (locMax.getX() + locMin.getX()) / 2;
        final double locZ = locMax.getZ();
        final double locY = locMax.getWorld().getHighestBlockAt((int) locX, (int) locZ).getY();
        final World world = locMax.getWorld();

        if(storage.storedLocation(claim.getID().toString())) {
            if(Config.COOLDOWN_ENABLE) {
                if(!cooldown.addCooldown(player)) { return true; }
            }
            if(warmup.isWarmup(player)) { return true; }
            final Location loc = storage.getLocation(claim.getID().toString());
            if(Config.WARMUP_ENABLE) {
                warmup.addWarmup(player, loc);
                return true;
            } else { player.teleport(loc); }
        } else {
            if(Config.COOLDOWN_ENABLE) {
                if(!cooldown.addCooldown(player)) { return true; }
            }
            if(warmup.isWarmup(player)) { return true; }
            final Location loc = new Location(world, locX, locY, locZ);
            if(Config.WARMUP_ENABLE) {
                warmup.addWarmup(player, loc);
                return true;
            } else { player.teleport(loc); }
        }

        MessageHandler.sendMessage(player, MessageHandler.placeholders(Messages.TELEPORT_TP, claim.getID().toString(), player.getName(), null, null), null, null);

        return true;
    }

    private boolean isNumber(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

}
