package net.southhollow.claims.commands.ClaimTP;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.MessageHandler;
import net.southhollow.claims.handler.tp.StorageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetClaimName implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            MessageHandler.sendConsole("&cThis command can only be used in-game.");
            return true;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        final StorageHandler storage = new StorageHandler();

        if(args.length == 0) {
            MessageHandler.sendMessage(player, Messages.SETNAME_NOARGS);
            return true;
        }

        if(!isLong(args[0])) {
            MessageHandler.sendMessage(player, Messages.UNVALID_NUMBER);
            return true;
        }

        for(final Claim claims : GriefPrevention.instance.dataStore.getPlayerData(uuid).getClaims()) {
            if(claims.getID().toString().equals(args[0])) {
                if(args.length < 2) {
                    MessageHandler.sendMessage(player, Messages.SETNAME_NONAME);
                    return true;
                } else {
                    storage.setName(claims.getID().toString(), args[1]);
                    MessageHandler.sendMessage(player, MessageHandler.placeholders(Messages.SETNAME_SETNAME, claims.getID().toString(), player.getName(), args[1], null), null, null);
                    return true;
                }
            }
        }

        MessageHandler.sendMessage(player, Messages.UNVALID_CLAIMID);

        return true;
    }

    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

}
