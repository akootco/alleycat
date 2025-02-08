package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.Text
import co.akoot.plugins.bluefox.util.Text.Companion.color
import co.akoot.plugins.bluefox.util.Text.Companion.hover
import co.akoot.plugins.bluefox.util.Text.Companion.titleCase
import co.akoot.plugins.bluefox.util.Text.Companion.invoke
import co.akoot.plugins.bluefox.util.Text.Companion.underlined
import co.akoot.plugins.bluefox.util.Text.EnumOption
import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class TestCommand(plugin: FoxPlugin) : FoxCommand(plugin, "test", "t") {

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        Text(sender) { "Hi lol"("accent") }
        Text(sender) { "Hi lol".underlined() }
        Text.broadcast { "Hello everyone!".hover("hover text :o") }
        return true
    }
}