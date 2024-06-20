package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.ColorUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.CommandSender

class TestCommand(plugin: FoxPlugin): FoxCommand(plugin, "test", "t") {
    override fun onTabComplete(sender: CommandSender, args: Array<out String>?): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, args: Array<out String>?): Boolean {
        sender.sendMessage(Component.text("win?").color(ColorUtil.getColor("error_text")))
        return true
    }
}