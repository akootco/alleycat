package co.akoot.plugins.alleycat.listeners

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.extensions.*
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.event.player.PlayerPickItemEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.*
import org.bukkit.event.server.ServerLoadEvent

class PlayerListener(val plugin: AlleyCat): Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerPickItem(event: PlayerAttemptPickupItemEvent) {
        event.isCancelled = !event.player.canPickUpItems
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAsyncPlayerChat(event: AsyncChatEvent) {
        event.isCancelled = event.player.isMuted
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        event.isCancelled = !event.player.canBreakBlocks
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerAttack(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        event.isCancelled = !(event.damager as Player).canAttack
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if(event.player.isSilentJoin) event.joinMessage(null)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerLeave(event: PlayerQuitEvent) {
        if(event.player.isSilentLeave) event.quitMessage(null)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if(event.player.isSilentLeave) event.deathMessage(null)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerAdvancementDone(event: PlayerAdvancementDoneEvent) {
        if(event.player.isSilentAdvancements) event.message(null)
    }
}