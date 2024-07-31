package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.Txt
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand(plugin: FoxPlugin) : FoxCommand(plugin, "test", "t") {

    override fun onTabComplete(sender: CommandSender, args: Array<out String>?): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        sendMessage(
            sender,
            "Hello @PLAYER, today is the #NUMBERst day of the year!",
            "PLAYER" to sender.name,
            "NUMBER" to 101
        )
        sendMessage(
            sender,
            "@PLAYER, today is the #NUMBERst day of the year!",
            "PLAYER" to sender.displayName(),
            "NUMBER" to 101
        )
        sender.sendMessage(Txt("hey").c)
        sender.sendMessage(Txt("hey", 0xff0000).c)
        sender.sendMessage(Txt("do not click here pal", 0xff0000).run("/kill").c)
        sender.sendMessage(Txt("do not click here pal", 0xff0000).hover("buddy..?").run("/kill").c)
        sender.sendMessage(Txt("do not click here pal", 0xff0000).hover(Txt("buddy..?", 0x00aa00)).run("/kill").c)
        return true
    }
}