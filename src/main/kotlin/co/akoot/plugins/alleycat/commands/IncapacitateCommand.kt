package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.extensions.isIncapacitated
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.names
import co.akoot.plugins.bluefox.util.Text.Companion.now
import co.akoot.plugins.bluefox.util.Text.Companion.plus
import org.bukkit.command.CommandSender

class IncapacitateCommand(private val ac: AlleyCat): FoxCommand(ac, "incapacitate") {

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return if(hasPermission(sender)) getOnlinePlayerSuggestions(args, ac.incapacitatedPlayers.names().toSet())
        else mutableListOf()
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        return when(alias) {
            "incapacitate" -> {
                return when(args.size) {
                    0 -> sendUsage(sender)
                    1 -> {
                        val player = getPlayer(args[0]).getAndSend(sender) ?: return false
                        val value = !player.isIncapacitated
                        player.isIncapacitated = value
                        Result.success(
                            player.displayName() + Kolor.TEXT(" is ") + Kolor.ACCENT(value.now) + Kolor.TEXT(" incapacitated.")
                        ).send(sender).value
                    }
                    else -> sendUsage(sender)
                }
            }
            else -> sendUsage(sender)
        }
    }
}