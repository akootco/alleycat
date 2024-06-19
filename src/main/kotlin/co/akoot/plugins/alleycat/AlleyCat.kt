package co.akoot.plugins.alleycat

import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.TextUtil
import com.destroystokyo.paper.profile.PlayerProfile
import io.papermc.paper.ban.BanListType
import net.kyori.adventure.text.Component
import org.bukkit.BanEntry
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class AlleyCat : FoxPlugin() {

    companion object {
    }

    override fun register() {
        logger.info("I'll see what I can do...")
        BlueFox.trace("hehehe.. - AlleyCat") // Test that PROVES we can reference BlueFox!!
    }

    override fun unregister() {
        // Plugin shutdown logic
    }

    fun ban(player: OfflinePlayer, source: String, reason: String = "Banned by an operator.", expiration: Date? = null): Boolean {
        if (player.isBanned) return false
        // player.ban<BanEntry<PlayerProfile>>("hey", Date(), "me")
        player.banPlayer(reason, expiration, source)
        return true
    }

    fun pardon(player: OfflinePlayer): Boolean {
        val banList = server.getBanList(BanListType.PROFILE)
        val entry = banList.getBanEntry(player.playerProfile) ?: return false
        entry.remove()
        return true
    }

//    fun getBanQuery(entry: BanEntry<PlayerProfile>, showCreated: Boolean = false, anonymous: Boolean = false, now: Long = System.currentTimeMillis()): Component {
//        val hasMessage = entry.reason != "Banned by an operator."
//        val hasExpiration = entry.expiration != null
//        return Component.text(entry.banTarget.name ?: "Player").color(COLOR_PLAYER)
//            .append(Component.text(" was banned by ").color(COLOR_TEXT))
//            .append(Component.text(if (anonymous) "CONSOLE" else  entry.source)).color(COLOR_PLAYER)
//            .append(Component.text(if(showCreated) " on " else "").color(COLOR_TEXT))
//            .append(if(showCreated) TimeUtil.getTimeComponent(entry.created.time, now) else TextUtil.EMPTY)
//            .append(Component.text(if(hasMessage) "\n\"${entry.reason}\"" else "").color(COLOR_ACCENT))
//            .append(Component.text(if(hasExpiration) "\nExpires: " else "").color(COLOR_TEXT))
//            .append(if(hasExpiration) entry.expiration?.time?.let { TimeUtil.getTimeComponent(it, now) } ?: Component.text("NEVER").color(COLOR_NUMBER) else TextUtil.EMPTY)
//    }
//
//    fun getBanQuery(player: OfflinePlayer, showCreated: Boolean = false, anonymous: Boolean = false, now: Long = System.currentTimeMillis()): Component {
//        val banList = server.getBanList(BanListType.PROFILE)
//        val entry = banList.getBanEntry(player.playerProfile) ?: return Component.text("${player.name} is not banned.").color(COLOR_ERROR)
//        return getBanQuery(entry, showCreated, anonymous, now)
//    }
}