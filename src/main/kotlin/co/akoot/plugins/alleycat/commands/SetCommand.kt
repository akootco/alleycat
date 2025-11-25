package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.extensions.Key
import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.getPDC
import co.akoot.plugins.bluefox.extensions.setPDC
import co.akoot.plugins.bluefox.util.Text
import org.bukkit.command.CommandSender

class SetCommand(plugin: AlleyCat): FoxCommand(plugin, "set") {
    override fun onTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when(args.size) {
            1 -> getOfflinePlayerSuggestions(args).also { it.add("\$world") }
            2 -> return mutableListOf(
               Key.CAN_PICK_UP_ITEMS,
               Key.IS_MUTED,
               Key.CAN_BREAK_BLOCKS,
               Key.CAN_PLACE_BLOCKS,
               Key.CAN_ATTACK,
               Key.IS_SILENT_JOIN,
               Key.IS_SILENT_LEAVE,
               Key.IS_SILENT_DEATH,
               Key.IS_SILENT_ADVANCEMENTS,
                "advancements_disabled"
            )
            3 -> mutableListOf("true", "false")
            else -> mutableListOf()
        }
    }

    override fun onCommand(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): Boolean {
        val player = (if(args.getOrNull(0) == $$"$world") getPlayerSender(sender).getAndSend(sender) else  runCatching { getPlayer(args[0]).getAndSend(sender) }.getOrNull()) ?: return false
        val key = runCatching { args[1] }.getOrNull()
        val value = runCatching { args[2].toBoolean() }.getOrNull()
        if(key == null || value == null) return sendUsage(sender)
        if(key == "advancements_disabled") {
            player.world.setPDC(plugin.key("advancements_disabled"), value)
        } else {
            player.setPDC<Boolean>(AlleyCat.key(key), value)
        }
        Text(sender) {
            Kolor.TEXT("Set ") + Kolor.ACCENT(key) + Kolor.TEXT(" to ") + Kolor.ACCENT(value.toString()) + Kolor.TEXT(" for ") + Kolor.ALT(player.name) + Kolor.TEXT(".")
        }
        return true
    }
}