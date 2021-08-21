package net.southhollow.claims.commands.ClaimTP;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.MessageHandler;
import net.southhollow.claims.handler.tp.StorageHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetClaimSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            MessageHandler.sendConsole("&cThis command can only be used in-game.");
            return true;
        }

        final Player player = (Player) sender;
        final Location loc = player.getLocation();
        final StorageHandler storage = new StorageHandler();
        final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);

        if (claim == null) {
            MessageHandler.sendMessage(player, Messages.SETSPAWN_OUTSIDE);
        } else {
            if(!claim.getOwnerName().equals(player.getName())) {
                MessageHandler.sendMessage(player, Messages.SETSPAWN_NOOWNER);

                return true;
            }

            final int locX = (int) loc.getX();
            final int locY = (int) loc.getY();
            final int locZ = (int) loc.getZ();
            final String world = Objects.requireNonNull(loc.getWorld()).getName();

            storage.setLocation(claim.getID().toString(), loc);

            final String locText = "X:" + locX + " Y:" + locY + " Z:" + locZ;

            MessageHandler.sendMessage(player, MessageHandler.placeholders(Messages.SETSPAWN_SETSPAWN, claim.getID().toString(), player.getName(), null, locText), world, null);

        }
        return true;
    }
}
