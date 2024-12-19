package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.FoxPlugin
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GamemodeCommand(plugin: FoxPlugin): FoxCommand(plugin, "gamemode", "Change a player's Gamemode", "/gamemode [mode] [player]") {

    companion object{
        fun setGameMode(sender: CommandSender, target: Player, gameMode: GameMode) {
            val gameModeName = gameMode.name.lowercase().replaceFirstChar(Char::titlecase)
            val message = if (sender == target) getMessage(
                "Set your Gamemode to \$GAME_MODE mode",
                "GAME_MODE" to gameModeName
            )
            else getMessage(
                "Set @TARGET's Gamemode to \$GAME_MODE mode",
                "GAME_MODE" to gameModeName,
                "TARGET" to target.name
            )
            target.gameMode = gameMode
            target.sendMessage(message)
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>?
    ): MutableList<String> {
        if (args == null) return mutableListOf()
        if (args.size == 1) return GameMode.entries.map { it.name.lowercase() }.toMutableList()
        else if (args.size > 1 && hasPermission(sender, "target")) return getOnlinePlayerSuggestions(args)
        return mutableListOf()
    }

    override fun onCommand(
        sender: CommandSender,
        alias: String,
        args: Array<out String>?
    ): Boolean {
        permissionCheck(sender) ?: return false
        if(args.isNullOrEmpty()) {
            val player = playerCheck(sender) ?: return false
            val gameMode = if (player.gameMode == GameMode.CREATIVE) GameMode.SURVIVAL
            else GameMode.CREATIVE
            setGameMode(sender, player, gameMode)
            return true
        }
        return true
    }
}