package co.akoot.plugins.alleycat.listeners

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.When
import co.akoot.plugins.alleycat.extensions.*
import co.akoot.plugins.bluefox.extensions.getPDC
import co.akoot.plugins.bluefox.extensions.isSurventure
import co.akoot.plugins.bluefox.util.sync
import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*

class PlayerListener(val plugin: AlleyCat): Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerPickItem(event: PlayerAttemptPickupItemEvent) {
        event.isCancelled = !event.player.canPickUpItems
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAsyncPlayerChat(event: AsyncChatEvent) {
        event.isCancelled = event.player.isMuted
        sync {
            event.player.executeWhen(When.Event.TALK)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        if (event.isCancelled) return
        event.isCancelled = !event.player.canBreakBlocks
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerAttack(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val player = event.damager as Player
        event.isCancelled = !(player).canAttack
        player.executeWhen(When.Event.ATTACK)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if(player.isSilentJoin) event.joinMessage(null)
        if(player.isSurventure) player.allowFlight = player.getPDC<Boolean>(plugin.key("permafly")) ?: player.allowFlight
        player.executeWhen(When.Event.JOIN)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        if(event.player.isSilentLeave) event.quitMessage(null)
        event.player.executeWhen(When.Event.LEAVE)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if(event.player.isSilentLeave) event.deathMessage(null)
        event.player.executeWhen(When.Event.DIE)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        if(event.player.isSilentAdvancements) event.message(null)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if(player.isSurventure) player.allowFlight = player.getPDC<Boolean>(plugin.key("permafly")) ?: player.allowFlight
        player.executeWhen(When.Event.RESPAWN)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        event.player.executeWhen(When.Event.TELEPORT)
    }

    @EventHandler
    fun onPlayerSleep(event: PlayerBedEnterEvent) {
        event.player.executeWhen(When.Event.SLEEP)
    }

    @EventHandler
    fun onPlayerGameModeChanged(event: PlayerGameModeChangeEvent) {
        val player = event.player
        if(player.isSurventure) player.allowFlight = player.getPDC<Boolean>(plugin.key("permafly")) ?: player.allowFlight
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerAdvancement(event: PlayerAdvancementCriterionGrantEvent) {
        if(event.player.world.getPDC<Boolean>(plugin.key("advancements_disabled")) == true) event.isCancelled = true
    }
}