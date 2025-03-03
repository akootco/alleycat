package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.extensions.isIncapacitated
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.extensions.names
import co.akoot.plugins.bluefox.util.Text.Companion.accented
import co.akoot.plugins.bluefox.util.Text.Companion.color
import co.akoot.plugins.bluefox.util.Text.Companion.error
import co.akoot.plugins.bluefox.util.Text.Companion.errorAccented
import co.akoot.plugins.bluefox.util.Text.Companion.nowAccented
import co.akoot.plugins.bluefox.util.Text.Companion.plus
import org.bukkit.command.CommandSender

class IncapacitateCommand(private val alleyCat: AlleyCat): FoxCommand(alleyCat, "incapacitate") {

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return if(hasPermission(sender)) getOnlinePlayerSuggestions(args, alleyCat.incapacitatedPlayers.names().toSet())
        else mutableListOf()
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        return when(alias) {
            "incapacitate" -> {
                return when(args.size) {
                    0 -> sendUsage(sender)
                    1 -> {
                        val player = plugin.server.getPlayer(args[0])
                            ?: return Result.fail("Player not found: ".error() + args[0].errorAccented()).send(sender).value
                        val value = !player.isIncapacitated
                        player.isIncapacitated = value
                        Result.success(
                            player.displayName() + " is ".color("text") + value.nowAccented + " incapacitated.".color("text")
                        ).send(sender).value
                    }
                    else -> sendUsage(sender)
                }
            }
            else -> sendUsage(sender)
        }
    }
}