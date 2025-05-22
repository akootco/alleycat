package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import org.bukkit.command.CommandSender

class HatCommand(plugin: AlleyCat): FoxCommand(plugin, "hat") {
    override fun onTabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): Boolean {
        val player = getPlayerSender(sender).getAndSend(sender) ?: return false
        val helmet = player.inventory.helmet
        player.inventory.helmet = player.inventory.itemInMainHand
        player.inventory.setItemInMainHand(helmet)
        player.updateInventory()
        return true
    }
}