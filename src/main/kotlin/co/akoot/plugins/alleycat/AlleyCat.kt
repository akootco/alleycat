package co.akoot.plugins.alleycat

import co.akoot.plugins.alleycat.commands.GamemodeCommand
import co.akoot.plugins.alleycat.commands.IncapacitateCommand
import co.akoot.plugins.alleycat.commands.TestCommand
import co.akoot.plugins.alleycat.commands.WhenCommand
import co.akoot.plugins.alleycat.extensions.isIncapacitated
import co.akoot.plugins.alleycat.listeners.PlayerListener
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.ColorUtil
import co.akoot.plugins.bluefox.util.TimeUtil
import com.destroystokyo.paper.profile.PlayerProfile
import io.papermc.paper.ban.BanListType
import net.kyori.adventure.text.Component
import org.bukkit.BanEntry
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.*

/**
 * A FoxPlugin that aims to take care of administration aspects of the server
 */
class AlleyCat : FoxPlugin("alleycat") {

    companion object {
        fun key(key: String): NamespacedKey  {
            return NamespacedKey("alleycat", key)
        }
    }

    override fun load() {
        logger.info("I'll see what I can do...!")
    }

    override fun unload() {
        // Plugin shutdown logic
    }

    override fun registerEvents() {
        registerEventListener(PlayerListener(this))
    }

    val incapacitatedPlayers: List<Player>
        get() = server.onlinePlayers.filter { it.isIncapacitated }

    /**
     * Bans a player
     *
     * @param player The player
     * @param source The person that banned the player
     * @param reason The reason the player is banned
     * @param expiration The date the ban expires. If null, it will never expire (unless pardoned) (Default = null)
     * @return true if the player is banned, false otherwise
     */
    fun ban(
        player: OfflinePlayer,
        source: String,
        reason: String = "Banned by an operator.",
        expiration: Date? = null
    ): Boolean {
        if (player.isBanned) return false
        player.ban<BanEntry<PlayerProfile>>(reason, expiration, source)
        return true
    }

    /**
     * Pardons a player
     *
     * @param player The player
     * @param reason The reason the player was pardoned. If null, a message won't be sent. (Default = null)
     * @return true if the player was pardoned (banned in the first place), false otherwise
     */
    fun pardon(player: OfflinePlayer, reason: String? = null): Boolean {
        val banList = server.getBanList(BanListType.PROFILE)
        val entry = banList.getBanEntry(player.playerProfile) ?: return false
        entry.remove()
        return true
    }

    /**
     * Register all the commands for this plugin
     */
    override fun registerCommands() {
        registerCommand(TestCommand(this))
        registerCommand(IncapacitateCommand(this))
        registerCommand(WhenCommand(this))
    }
}