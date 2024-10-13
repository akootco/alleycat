package co.akoot.plugins.alleycat

import co.akoot.plugins.alleycat.commands.TestCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.ColorUtil
import co.akoot.plugins.bluefox.util.TextUtil
import co.akoot.plugins.bluefox.util.TimeUtil
import com.destroystokyo.paper.profile.PlayerProfile
import io.papermc.paper.ban.BanListType
import net.kyori.adventure.text.Component
import org.bukkit.BanEntry
import org.bukkit.OfflinePlayer
import java.util.*

/**
 * A FoxPlugin that aims to take care of administration aspects of the server
 */
class AlleyCat : FoxPlugin() {

    override fun load() {
        logger.info("I'll see what I can do...!")
    }

    override fun unload() {
        // Plugin shutdown logic
    }

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
    }

    /**
     *
     */
    fun getBanQuery(
        entry: BanEntry<PlayerProfile>,
        showCreated: Boolean = false,
        anonymous: Boolean = false,
        now: Long = System.currentTimeMillis()
    ): Component {
        val hasMessage = entry.reason != "Banned by an operator."
        val hasExpiration = entry.expiration != null
        return Component.text(entry.banTarget.name ?: "Player").color(ColorUtil.getColor("player"))
            .append(Component.text(" was banned by ").color(ColorUtil.getColor("text")))
            .append(Component.text(if (anonymous) "CONSOLE" else entry.source)).color(ColorUtil.getColor("player"))
            .append(Component.text(if (showCreated) " on " else "").color(ColorUtil.getColor("text")))
            .append(if (showCreated) TimeUtil.getTimeComponent(entry.created.time, now) else Component.empty())
            .append(Component.text(if (hasMessage) "\n\"${entry.reason}\"" else "").color(ColorUtil.getColor("accent")))
            .append(Component.text(if (hasExpiration) "\nExpires: " else "").color(ColorUtil.getColor("text")))
            .append(if (hasExpiration) entry.expiration?.time?.let { TimeUtil.getTimeComponent(it, now) }
                ?: Component.text("NEVER").color(ColorUtil.getColor("number")) else Component.empty())
    }

    /**
     *
     */
    fun getBanQuery(
        player: OfflinePlayer,
        showCreated: Boolean = false,
        anonymous: Boolean = false,
        now: Long = System.currentTimeMillis()
    ): Component {
        val banList = server.getBanList(BanListType.PROFILE)
        val entry = banList.getBanEntry(player.playerProfile) ?: return Component.text("${player.name} is not banned.")
            .color(ColorUtil.getColor("error_text"))
        return getBanQuery(entry, showCreated, anonymous, now)
    }
}