package co.akoot.plugins.alleycat

import org.bukkit.plugin.java.JavaPlugin

class AlleyCat : JavaPlugin() {

    override fun onEnable() {
        logger.info("I'll see what I can do...")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}