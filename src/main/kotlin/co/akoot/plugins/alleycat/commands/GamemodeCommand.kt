package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.util.Text
import co.akoot.plugins.bluefox.util.Text.Companion.get
import co.akoot.plugins.bluefox.util.Text.Companion.plus
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GamemodeCommand(plugin: FoxPlugin): FoxCommand(
    plugin,
    "gamemode",
    "Change a player's Gamemode",
    "/gamemode [mode] [player]",
    "gm"
) {

    companion object{
        fun setGameMode(sender: CommandSender, target: Player, gameMode: GameMode) {
            val gameModeName = gameMode.name.lowercase().replaceFirstChar(Char::titlecase)
            val self = sender == target
            Text(sender) {
                Kolor.TEXT("Set ") + Kolor.ALT(self.get("your", target.name)) + Kolor.TEXT(" gamemode to ") + Kolor.ACCENT(gameModeName)
            }
            target.gameMode = gameMode
        }
    }

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
        if (args.size == 1) return GameMode.entries.map { it.name.lowercase() }.toMutableList()
        else if (args.size > 1 && hasPermission(sender, "target")) return getOnlinePlayerSuggestions(args)
        return mutableListOf()
    }

    override fun onCommand(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): Boolean {
        permissionCheck(sender) ?: return false
        if(args.isEmpty()) {
            val player = playerCheck(sender) ?: return false
            val gameMode = if (player.gameMode == GameMode.CREATIVE) GameMode.SURVIVAL
            else GameMode.CREATIVE
            setGameMode(sender, player, gameMode)
        } else if(args.size == 1) {
            val player = playerCheck(sender) ?: return false
            val gameMode = GameMode.entries.find { it.name.startsWith(args[0], true) } ?: return sendUsage(sender)
            setGameMode(sender, player, gameMode)
        }
        return true
    }
}