package co.akoot.plugins.alleycat.api

import co.akoot.plugins.bluefox.BlueFox
import co.akoot.plugins.bluefox.extensions.mkdirp
import co.akoot.plugins.bluefox.util.IOUtil

class UpdatablePlugin(val downloadURL: String, val destination: String = "plugins") {
    fun download(fileName: String): Boolean {
        return try {
            val destination = BlueFox.instance.dataFolder.resolve(destination).mkdirp().resolve(fileName)
            IOUtil.downloadFile(downloadURL, destination)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}