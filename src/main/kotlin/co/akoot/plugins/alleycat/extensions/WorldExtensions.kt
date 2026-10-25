package co.akoot.plugins.alleycat.extensions

import co.akoot.plugins.alleycat.AlleyCat
import co.akoot.plugins.alleycat.When
import co.akoot.plugins.bluefox.extensions.addToPDCList
import co.akoot.plugins.bluefox.extensions.getPDCList
import co.akoot.plugins.bluefox.extensions.removeFromPDCList
import org.bukkit.OfflinePlayer
import org.bukkit.World

fun World.addWhen(player: OfflinePlayer, whenDefinition: When): Boolean =
    addWhen(player.uniqueId.toString(), whenDefinition)

fun World.addWhen(name: String, whenDefinition: When): Boolean {
    return addToPDCList(AlleyCat.key("when.$name"), whenDefinition.toString())
}

fun World.removeWhen(player: OfflinePlayer, whenDefinition: When): Boolean {
    return removeWhen(player, whenDefinition.toString())
}

fun World.removeWhen(player: OfflinePlayer, whenDefinitionString: String): Boolean {
    return removeFromPDCList(AlleyCat.key("when.${player.uniqueId}"), whenDefinitionString)
}

fun World.getWhenDefinitions(player: OfflinePlayer, event: When.Event? = null): List<When>? =
    getWhenDefinitions(player.uniqueId.toString(), event)

fun World.getWhenDefinitions(name: String, event: When.Event? = null): List<When>? {
    val definition = getPDCList<String>(AlleyCat.key("when.$name"))
        ?.mapNotNull { When.of(it) } ?: return null
    return if (event == null) definition
    else definition.filter { it.event == event }
}