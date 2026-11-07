package co.akoot.plugins.alleycat.api

object PluginUpdater {
    private val plugins = mutableSetOf<UpdatablePlugin>()
    fun register(plugin: UpdatablePlugin) = plugins.add(plugin)
    fun unregister(plugin: UpdatablePlugin) = plugins.remove(plugin)
}