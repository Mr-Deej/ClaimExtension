package net.southhollow.claims.handler;

import net.southhollow.claims.config.Config;
import net.southhollow.claims.listener.CombatMode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CombatScheduler extends BukkitRunnable {

    @Override
    public void run() {
        if(CombatMode.TIME.isEmpty()) { return; }

        for(final UUID uuid : CombatMode.TIME.keySet()) {
            final OfflinePlayer victim = Bukkit.getOfflinePlayer(uuid);
            final OfflinePlayer attacker = Bukkit.getOfflinePlayer(CombatMode.ATTACKER.get(uuid));
            final long time = CombatMode.TIME.get(uuid);
            final long newTime = System.currentTimeMillis() / 1000;
            final long combatTime = newTime - time;
            final long combatLeft = Config.COMBAT_TIME - combatTime;

            if(!victim.isOnline() || !attacker.isOnline()) {
                CombatMode.TIME.remove(uuid);
                CombatMode.ATTACKER.remove(uuid);
            }
            else if(combatTime >= Config.COMBAT_TIME) {
                CombatMode.TIME.remove(uuid);
                CombatMode.ATTACKER.remove(uuid);
            }
            else {
                if(Config.TIMER_ENABLED) {
                    MessageHandler.sendAction(victim.getPlayer(), "&4&lVICTIM " + "== " + combatLeft + " SEC ==");
                    MessageHandler.sendAction(attacker.getPlayer(), "&4&lATTACKER " + "== " + combatLeft + " SEC ==");
                }

            }
        }
    }
}
