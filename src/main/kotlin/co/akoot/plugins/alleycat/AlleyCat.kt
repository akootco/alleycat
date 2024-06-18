package co.akoot.plugins.alleycat

import co.akoot.plugins.bluefox.BlueFox
import org.bukkit.plugin.java.JavaPlugin

class AlleyCat : JavaPlugin() {

    override fun onEnable() {
        logger.info("I'll see what I can do...")
        BlueFox.trace("hehehe.. - AlleyCat") // Test that PROVES we can reference BlueFox!!
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}