package co.akoot.plugins.alleycat

import co.akoot.plugins.alleycat.extensions.removeWhen
import co.akoot.plugins.bluefox.BlueFox
import org.bukkit.Bukkit
import org.bukkit.entity.Player

data class When(val event: Event, val timeFrame: TimeFrame, val type: Type, val command: String) {

    companion object {
        fun of(string: String): When? {
            try {
                val data = string.split(";")
                val event = Event.entries[data[0].toInt()]
                val timeFrame = TimeFrame.entries[data[1].toInt()]
                val type = Type.entries[data[2].toInt()]
                val command = data.drop(3).joinToString(";")
                return When(event, timeFrame, type, command)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        fun event(string: String): Event? {
            return runCatching { Event.valueOf(string.split(";")[0]) }.getOrNull()
        }

        fun timeFrame(string: String): TimeFrame? {
            return runCatching { TimeFrame.valueOf(string.split(";")[1]) }.getOrNull()
        }

        fun type(string: String): Type? {
            return runCatching { Type.valueOf(string.split(";")[2]) }.getOrNull()
        }

        fun command(string: String): Type? {
            return runCatching { Type.valueOf(string.split(";").drop(3).joinToString(";")) }.getOrNull()
        }
    }
    enum class Event {
        JOIN, LEAVE, DIE, RESPAWN, TELEPORT, TALK, ATTACK, SLEEP
    }

    enum class TimeFrame {
        NEXT, ALWAYS
    }

    enum class Type {
        RUN, COMPEL
    }

    override fun toString(): String {
        return "${event.ordinal};${timeFrame.ordinal};${type.ordinal};$command"
    }

    fun toSuggestion(): String {
        return "${event.name.lowercase()}.${timeFrame.name.lowercase()}.${type.name.lowercase()}.${command.split(" ")[0]}"
    }

    fun execute(player: Player) {
        val commandLine = command.replace("@s", player.name)
        if(type == Type.RUN) {
            player.server.dispatchCommand(Bukkit.getConsoleSender(), commandLine)
        } else {
            player.server.dispatchCommand(player, commandLine)
        }
        if(timeFrame == TimeFrame.NEXT) BlueFox.world?.removeWhen(player, this)
    }


}
