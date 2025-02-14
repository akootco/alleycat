package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.util.Text
import co.akoot.plugins.bluefox.util.Text.Companion.color
import co.akoot.plugins.bluefox.util.Text.Companion.hover
import co.akoot.plugins.bluefox.util.Text.Companion.invoke
import co.akoot.plugins.bluefox.util.Text.Companion.noShadow
import co.akoot.plugins.bluefox.util.Text.Companion.underlined
import co.akoot.plugins.bluefox.util.color
import co.akoot.plugins.bluefox.util.shadow
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand(plugin: FoxPlugin) : FoxCommand(plugin, "test", "t") {

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        Text(sender) { "Hi lol"("accent") }
        Text(sender) { "Hi lol".underlined() }
        Text.broadcast { "Hello everyone!".hover("hover text :o") }
        Text(sender) { "what is all this".color("#00ffff".color, "#ff0000".shadow(1.0)).boldItalic() }
        Text(sender) { "what is all this".color("#00ffff".color, "#ff000055".shadow).boldItalic() }
        Text(sender) { "lol".noShadow() }
        Text { "hello".color("#ff0000".color, "#00ffff80".shadow) }
        Text { "hello".color("#ff0000".color, "#00ffff".shadow(0.5)) }
        return true
    }
}