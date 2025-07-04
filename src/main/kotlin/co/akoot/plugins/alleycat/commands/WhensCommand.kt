package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.When
import co.akoot.plugins.alleycat.When.*
import co.akoot.plugins.alleycat.extensions.addWhen
import co.akoot.plugins.alleycat.extensions.getWhenDefinitions
import co.akoot.plugins.alleycat.extensions.removeWhen
import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.getPDCList
import co.akoot.plugins.bluefox.extensions.invoke
import co.akoot.plugins.bluefox.extensions.setMeta
import co.akoot.plugins.bluefox.extensions.setPDC
import co.akoot.plugins.bluefox.util.Text
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

class WhensCommand(private val ac: AlleyCat): FoxCommand(ac, "whens") {

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        return when(args.size) {
            1 -> mutableListOf("remove", "list")
            2 -> getOfflinePlayerSuggestions(args)
            3 -> {
                val player = getOfflinePlayer(args[1]).value ?: return nothing
                val whens = BlueFox.world?.getWhenDefinitions(player) ?: return nothing
                whens.map { it.toSuggestion() }.toMutableList()
            }
            else -> nothing
        }
    }

    override fun onCommand(sender: CommandSender, alias: String, args: Array<out String>): Boolean {
        return when(args.size) {
            2 -> {
                if(args[0] != "list") return sendUsage(sender)
                val player = getOfflinePlayer(args[1]).getAndSend(sender) ?: return false
                val whens = getWhens(player) ?: return false
                Text(sender) {
                    Kolor.TEXT("[") + Kolor.PLAYER(player.name ?: "???") + Kolor.TEXT("]")
                }
                for(whenText in whens) {
                    sender.sendMessage(whenText.component)
                }
                return true
            }
            3 -> {
                if(args[0] != "remove") return sendUsage(sender)
                val player = getOfflinePlayer(args[1]).getAndSend(sender) ?: return false
                val whens = BlueFox.world?.getWhenDefinitions(player)?.filter { it.toSuggestion() == args[2] } ?: return false
                if(whens.isEmpty()) {
                    Text(sender) {
                        Kolor.ERROR("Could not find any whens!")
                    }
                    return false
                }
                whens.forEach { BlueFox.world?.removeWhen(player, it) }
                Result.success(Kolor.TEXT("Deleted all specified whens for ") + Kolor.PLAYER(player.name ?: "them") + Kolor.TEXT("!")).getAndSend(sender)
            }
            else -> sendUsage(sender)
        }
    }

    private fun getWhens(player: OfflinePlayer): MutableList<Text>? {
        val whens = BlueFox.world?.getWhenDefinitions(player) ?: return null
        val texts = mutableListOf<Text>()
        for(whenDef in whens) {
            texts += (Kolor.ACCENT(whenDef.event.name) + " " + Kolor.ALT(whenDef.timeFrame.name) + " " + Kolor.NUMBER(whenDef.type.name) + " " + Kolor.QUOTE("/${whenDef.command}"))
                .hover(Kolor.ERROR("Click to remove"))
                .execute("whens remove ${player.name} ${whenDef.toSuggestion()}")
        }
        return texts
    }
}