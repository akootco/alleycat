package co.akoot.plugins.alleycat

import co.akoot.plugins.alleycat.commands.AtCommand
import co.akoot.plugins.alleycat.commands.FlyCommand
import co.akoot.plugins.alleycat.commands.GamemodeCommand
import co.akoot.plugins.alleycat.commands.HatCommand
import co.akoot.plugins.alleycat.commands.IncapacitateCommand
import co.akoot.plugins.alleycat.commands.SetCommand
import co.akoot.plugins.alleycat.commands.TestCommand
import co.akoot.plugins.alleycat.commands.WhenCommand
import co.akoot.plugins.alleycat.commands.WhensCommand
import co.akoot.plugins.alleycat.extensions.isIncapacitated
import co.akoot.plugins.alleycat.listeners.PenjaminListener
import co.akoot.plugins.alleycat.listeners.PlayerListener
import co.akoot.plugins.bluefox.api.FoxConfig
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.ColorUtil
import co.akoot.plugins.bluefox.util.TimeUtil
import co.akoot.plugins.bluefox.util.async
import com.destroystokyo.paper.profile.PlayerProfile
import io.papermc.paper.ban.BanListType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
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

    var penjamin: JDA? = null
    val dmRecipients: MutableList<User> = mutableListOf()
    lateinit var auth: FoxConfig

    companion object {
        lateinit var instance: AlleyCat
        fun key(key: String): NamespacedKey  {
            return NamespacedKey("alleycat", key)
        }
    }

    override fun load() {
        instance = this
        penjamin = createPenjamin()
        async {
            penjamin?.awaitReady()
            for (id in settings.getLongList("penjamin.recipients")) {
                penjamin?.retrieveUserById(id)?.queue {
                    dmRecipients.add(it)
                    println("adding $id to penjamin's list")
                }
            }
        }
        penjamin?.addEventListener(PenjaminListener(this))
        logger.info("I'll see what I can do...!")
    }

    override fun unload() {
        // Plugin shutdown logic
    }

    fun createPenjamin(): JDA? {
        val token = auth.getString("discord.token")
        if (token == null) {
            logger.severe("Invalid Discord Token in auth.conf, Discord features disabled.")
            return null
        }
        val builder = JDABuilder.createDefault(token)
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS)
        return try {
            builder.build()
        } catch (e: Exception) {
            logger.severe("Could not load JDA, Discord features disabled.")
            e.printStackTrace()
            null
        }
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
//        registerCommand(TestCommand(this))
        registerCommand(IncapacitateCommand(this))
        registerCommand(WhenCommand(this))
        registerCommand(WhensCommand(this))
        registerCommand(SetCommand(this))
        registerCommand(FlyCommand(this))
        //registerCommand(AtCommand(this))
//        registerCommand(GamemodeCommand(this))
//        registerCommand(HatCommand(this))
    }

    override fun registerConfigs() {
        auth = registerConfig("auth")
    }
}