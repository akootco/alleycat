package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.When
import co.akoot.plugins.alleycat.When.*
import co.akoot.plugins.alleycat.extensions.addWhen
import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.api.CatCommand
import co.akoot.plugins.bluefox.api.FoxCommand
import co.akoot.plugins.bluefox.api.Kolor
import co.akoot.plugins.bluefox.extensions.invoke
import co.akoot.plugins.bluefox.util.primary
import co.akoot.plugins.bluefox.util.quote
import co.akoot.plugins.bluefox.util.secondary
import co.akoot.plugins.bluefox.util.sendError
import co.akoot.plugins.bluefox.util.sendText
import co.akoot.plugins.bluefox.util.sendWarning
import co.akoot.plugins.bluefox.util.tertiary
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

object WhenCommand: CatCommand(AlleyCat.instance, "when", "Do stuff when players do stuff!", onCommand = {
    then {
        string("name", suggestions = {_,builder->suggest(builder, BlueFox.cachedOfflinePlayerNames.apply { addAll(listOf(
            "--newPlayer"
        )) }, usernames = true)}) {
            false
        } then {
            word("event", suggestions = {_,builder->suggest(builder, Event.entries.map { it.name.lowercase() + "s" })}) {
                false
            } then {
                word("time frame", suggestions = {_,builder->suggest(builder, TimeFrame.entries.map { it.name.lowercase() })}) {
                    false
                } then {
                    word("type", suggestions = {_,builder->suggest(builder, Type.entries.map { it.name.lowercase() })}) {
                        false
                    } then {
                        greedyString("command") {
                            it.permissionCheck() ?: return@greedyString false
                            val name = it.string("name")
                            val player = BlueFox.getOfflinePlayer(name)
                            try {
                                val event = Event.valueOf(it.string("event").removeSuffix("s").uppercase())
                                val timeFrame = TimeFrame.valueOf(it.string("time frame").uppercase())
                                val type = Type.valueOf(it.string("type").uppercase())
                                val command = it.string("command")
                                if (player == null) {
                                    if(name.startsWith("#")) {
                                        BlueFox.world?.addWhen(name, When(event, timeFrame, type, command)) ?: false
                                    } else {
                                        it.sender.sendError("Uhhhh, say mack... Put a real person here maybe? Not ", quote("'$name'"), "...")
                                    }
                                } else {
                                    BlueFox.world?.addWhen(player, When(event, timeFrame, type, command)) ?: false
                                }
                                it.sender.sendText(
                                    primary(if(timeFrame == TimeFrame.ALWAYS) "From now on and henceforth, when" else "Next time"),
                                    " ",
                                    secondary(name),
                                    " ",
                                    tertiary(event.name.lowercase() + "s"),
                                    ", ", if(type == Type.COMPEL) "they" else "we",
                                    " shall ", quote(command)
                                )
                            } catch(e: Exception) {
                                e.printStackTrace()
                                it.sender.sendWarning("Couldn't add this when. It's probably your fault!")
                            }
                        }
                    }
                }
            }
        }
    }
})