package co.akoot.plugins.alleycat.commands

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.bluefox.api.CatCommand

class AtCommand(plugin: AlleyCat): CatCommand(plugin, "at") {
    init {
        then(word("time").then(subcommand("am/pm").then(timeZone()
            .then(subcommand("run").then(greedyString("command").executes {
                val time = getString(it, "time")
                val amPm = getString(it, "am/pm")
                val zoneId = getZoneId(it)
                val command = getString(it, "command")
                getSender(it).sendMessage("Ok, at $time $amPm $zoneId, we shall run /$command")
                win
            }))))
        )
    }
}