package net.southhollow.claims.listener;

import net.southhollow.claims.config.Messages;
import net.southhollow.claims.handler.MessageHandler;
import net.southhollow.claims.handler.tp.Cooldown;
import net.southhollow.claims.handler.tp.Warmup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
            final Warmup warmup = new Warmup();
            final Cooldown cooldown = new Cooldown();
            if (warmup.isWarmup(e.getPlayer())) {
                warmup.moved(e.getPlayer());
                cooldown.remove(e.getPlayer());
                MessageHandler.sendTitle(e.getPlayer(), Messages.WARMUP_CANCELLED, "");
            }
        }
    }

}
