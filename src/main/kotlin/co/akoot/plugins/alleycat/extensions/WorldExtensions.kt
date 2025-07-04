package co.akoot.plugins.alleycat.extensions

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.When
import co.akoot.plugins.bluefox.extensions.addToPDCList
import co.akoot.plugins.bluefox.extensions.getPDCList
import co.akoot.plugins.bluefox.extensions.removeFromPDCList
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Player

fun World.addWhen(player: OfflinePlayer, whenDefinition: When): Boolean {
    return addToPDCList(AlleyCat.key("when.${player.uniqueId}"), whenDefinition.toString())
}

fun World.removeWhen(player: OfflinePlayer, whenDefinition: When): Boolean {
    return removeWhen(player, whenDefinition.toString())
}

fun World.removeWhen(player: OfflinePlayer, whenDefinitionString: String): Boolean {
    return removeFromPDCList(AlleyCat.key("when.${player.uniqueId}"), whenDefinitionString)
}

fun World.getWhenDefinitions(player: OfflinePlayer, event: When.Event? = null): List<When>? {
    val definition =  getPDCList<String>(AlleyCat.key("when.${player.uniqueId}"))
        ?.mapNotNull { When.of(it) } ?: return null
    return if(event == null) definition
    else definition.filter { it.event == event }
}