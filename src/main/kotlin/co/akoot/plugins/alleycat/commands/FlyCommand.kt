package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.setPDC
import co.akoot.plugins.bluefox.util.Text
import co.akoot.plugins.bluefox.util.Text.Companion.get
import org.bukkit.command.CommandSender

class FlyCommand(plugin: AlleyCat): FoxCommand(plugin, "fly", aliases = arrayOf("permafly")) {
    override fun onTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return getOnlinePlayerSuggestions(args, args.toSet())
    }

    override fun onCommand(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): Boolean {
        if(args.isEmpty()) {
            val player = getPlayerSender(sender).getAndSend(sender) ?: return false
            val toggle: Boolean = !player.allowFlight
            player.allowFlight = toggle
            Text(sender) {
                Kolor.TEXT(toggle.get("Enabled", "Disabled")) + Kolor.TEXT(" flight")
            }
            if(alias == "permafly") player.setPDC(plugin.key("permafly"), toggle)
            return true
        }
        var success = false
        var toggle = true
        val players = mutableListOf<String>()
        for(arg in args) {
            val player = getPlayer(arg).value ?: continue
            players += player.name
            toggle = !player.allowFlight
            player.allowFlight = toggle
            success = true
        }
        if(success) {
            Text(sender) {
                Kolor.TEXT(toggle.get("Enabled", "Disabled")) + Kolor.TEXT(" flight ") + Kolor.TEXT("for ") + Text.list(players)
            }
            return true
        } else {
            Text(sender) {
                Kolor.ERROR("No players found!")
            }
            return false
        }
    }
}