package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.invoke
import co.akoot.plugins.bluefox.util.Text
import co.akoot.plugins.bluefox.util.Text.Companion.plus
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand(plugin: FoxPlugin) : FoxCommand(plugin, "test", "t") {

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val x = Kolor.TEXT("Welcome pardner, we are now ").hover("fail") + Kolor.NUMBER("10") + Kolor.TEXT(" years away from completion!")
        println(x.json)
        Text(sender) {
            Kolor.WARNING("Welcome pardner, we are now ").hover("fail") + Kolor.WARNING.number("10") + Kolor.WARNING(" years away from completion!")
        }
        Text(sender) {
            Kolor.ERROR("Welcome pardner, we are now ").hover("fail") + Kolor.ERROR.number("10") + Kolor.ERROR(" years away from completion!")
        }
        return true
    }
}