package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.Text
import co.akoot.plugins.bluefox.util.Text.Companion.titleCase
import co.akoot.plugins.bluefox.util.Text.EnumOption
import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class TestCommand(plugin: FoxPlugin) : FoxCommand(plugin, "test", "t") {

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        sendMessage(
            sender,
            "Hello @PLAYER, today (\$WEST) is the #{NUMBER}st day of the year!",
            "PLAYER" to sender.name,
            "NUMBER" to 101,
            "TEST" to "nice",
            "WEST" to "west"
        )
        sendMessage(
            sender,
            "Hello @PLAYER, today is the #{NUMBER}st day of the year (this is a)!",
            "PLAYER" to sender.displayName(),
            "NUMBER" to 101
        )
        sendMessage(
            sender,
            "Hello."
        )
        sendMessage(sender, "Hello @PLAYER", "PLAYER" to sender.name)
        sendMessage(sender, "@PLAYER hello", "PLAYER" to sender.name)
        sendMessage(sender, "It is now \$TIME", "TIME" to BlueFox.settings.getLong("time"))
        BlueFox.settings.set("time", System.currentTimeMillis())
        (Text("Click ") + Text("HERE", "accent").underlined().hover("yes").suggest("win") + Text(" to WIN!")).send(sender)
        (Text("Click ") + Text("HERE", "error_accent").underlined().strikethrough().hover("no").suggest("die") + Text(" to WIN!")).broadcast()
        (Text("win!") * 3).broadcast()
        (Text() + "There are " + Random.nextInt() + " more seconds to go.").broadcast()
        Text.newline(sender)
        val item = sender.inventory.itemInMainHand
        (Text(EnumOption.TITLE_CASE, EnumOption.SPACES) + sender + " is holding " + item.amount + " " + item + " which is of type: " + item.type + "!").boldItalic().send(sender)
        return true
    }
}