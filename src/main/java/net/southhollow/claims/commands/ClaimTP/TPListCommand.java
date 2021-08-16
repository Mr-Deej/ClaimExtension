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

import java.util.UUID;

public class TPListCommand implements CommandExecutor {

    int countTo = 5;
    int countFrom = 0;
    int number = 1;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            MessageHandler.sendConsole("&cThis command can only be used in-game.");
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        final int maxClaims = GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().size();
        final StorageHandler storage = new StorageHandler();

        if(args.length != 0) {
            if(isInt(args[0])) {
                this.number = Integer.parseInt(args[0]);
                this.countTo = 5 * number;
                this.countFrom = (5 * number) - 5;
            } else {
                MessageHandler.sendMessage(player, Messages.UNVALID_NUMBER);
                return true;
            }
        }

        MessageHandler.sendMessage(player, MessageHandler.placeholders(Messages.LIST_HEADER, null, player.getDisplayName(), null, null), null, null);

        if(maxClaims == 0) {
            MessageHandler.sendMessage(player, Messages.LIST_NOCLAIM);
        }

        final int totalPage = (maxClaims / 5) + 1;

        for(int i = 0; i < GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().toArray().length; i++) {
            if(this.number > totalPage || this.number == 0) {
                this.countTo = 5 * totalPage;
                this.countFrom = (5 * totalPage) - 5;
                this.number = totalPage;
            }

            if(i >= this.countFrom) {
                final Claim claims = (Claim) GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims().toArray()[i];

                final Location locMin = claims.getLesserBoundaryCorner();
                final Location locMax = claims.getGreaterBoundaryCorner();
                final int locX = (int) ((locMin.getX()+locMax.getX()) / 2);
                final int locZ = (int) ((locMin.getZ()+locMax.getZ()) / 2);

                if(storage.stored(claims.getID().toString())) {
                    final String name = storage.getName(claims.getID().toString());
                    MessageHandler.sendMessage(player, Messages.LIST_CLAIMID + " " + Messages.LIST_CLAIMID_COLOR + claims.getID().toString() + " " + Messages.LIST_NAME + " " + Messages.LIST_NAME_COLOR + name + " " + Messages.LIST_COORDS + " " + Messages.LIST_COORDS_COLOR + "X:" + locX + " Z:" + locZ);
                } else {
                    MessageHandler.sendMessage(player, Messages.LIST_CLAIMID + " " + Messages.LIST_CLAIMID_COLOR + claims.getID().toString() + " " + Messages.LIST_NAME + " " + Messages.LIST_NAME_COLOR + "None" + " " + Messages.LIST_COORDS + " " + Messages.LIST_COORDS_COLOR + "X:" + locX + " Z:" + locZ);
                }


                if(i == this.countTo - 1) {
                    MessageHandler.sendMessage(player, "");
                    MessageHandler.sendMessage(player, "&e<--- [&6" + this.number + "\\" + totalPage + "&e] --->");
                    break;
                }

                continue;
            }
        }

        if(this.number == totalPage) {
            MessageHandler.sendMessage(player, "");
            MessageHandler.sendMessage(player, "&e<--- [&6" + totalPage + "\\" + totalPage + "&e] --->");
        }

        return true;
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
