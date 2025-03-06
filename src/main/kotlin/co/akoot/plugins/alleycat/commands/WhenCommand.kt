package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.invoke
import org.bukkit.command.CommandSender

class WhenCommand(private val ac: AlleyCat): FoxCommand(ac, "when") {

    enum class Event {
        JOIN, QUIT, DIE, RESPAWN, TELEPORT, CHAT, ATTACK, SLEEP
    }

    enum class TimeFrame {
        ONCE, ALWAYS
    }

    enum class Type {
        RUN, COMPEL
    }

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return when(args.size) {
            1 -> getOfflinePlayerSuggestions(args)
            2 -> Event.entries.map { "${it.name.lowercase()}s" }.toMutableList()
            3 -> TimeFrame.entries.map { it.name.lowercase() }.toMutableList()
            4 -> Type.entries.map { it.name.lowercase() }.toMutableList()
            else -> {
                val commandLine = args.drop(4).joinToString(" ")
                plugin.server.commandMap.tabComplete(sender, commandLine.removePrefix("/")) ?: mutableListOf()
            }
        }
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        return if (args.size < 5) sendUsage(sender)
        else {
            val player = getOfflinePlayer(args[0]).getAndSend(sender) ?: return false
            val name = player.name ?: return Result.fail(Kolor.ERROR("Player \"") + Kolor.ERROR.accent(args[0]) + Kolor.ERROR("\" not found!")).getAndSend(sender)
            val event = runCatching { Event.valueOf(args[1].uppercase().removeSuffix("S")) }.getOrNull() ?: return sendUsage(sender)
            val timeFrame = runCatching { TimeFrame.valueOf(args[2].uppercase()) }.getOrNull() ?: return sendUsage(sender)
            val type = runCatching { Type.valueOf(args[3].uppercase()) }.getOrNull() ?: return sendUsage(sender)
            val commandLine = args.drop(4).joinToString(" ")
            val timeFrameString = when(timeFrame) {
                TimeFrame.ONCE -> "Next time "
                TimeFrame.ALWAYS -> "From this time forward and henceforth, whenever "
            }
            val typeString = when(type) {
                Type.RUN -> "we"
                Type.COMPEL -> "they"
            }
            val message = Kolor.TEXT(timeFrameString) +
                    Kolor.PLAYER(name) +
                    Kolor.ACCENT(" ${event.name.lowercase()}s") +
                    Kolor.TEXT(", $typeString will run ") +
                    Kolor.QUOTE(commandLine) +
                    Kolor.TEXT(".")

            Result.success(message).getAndSend(sender)
        }
    }
}