package co.akoot.plugins.alleycat.listeners

import co.akoot.plugins.alleycat.AlleyCat
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PenjaminListener(private val plugin: AlleyCat) : ListenerAdapter() {
    // todo: improve
    val userMessageMap = mutableMapOf<Long, MutableSet<Long>>()
    val messageMap = mutableMapOf<Long, MutableSet<Long>>()
    val limit = plugin.settings.getInt("penjamin.limit") ?: 5

    fun reply(id: Long, message: Message) {
        val messageId = messageMap.entries.find { id in it.value }?.key ?: return
        val userId = userMessageMap.entries.find { messageId in it.value }?.key ?: return
        plugin.penjamin?.openPrivateChannelById(userId)?.queue { channel ->
            channel.retrieveMessageById(messageId).queue { originalMessage ->
                val finalMessage = message.contentRaw + message.attachments.joinToString("\n", prefix = "\n") { it.url }
                originalMessage.reply(finalMessage).addEmbeds(message.embeds).addComponents(message.components).queue {
                    message.addReaction(Emoji.fromUnicode("\uD83D\uDCE8")).queue()
                }
            }
        }
    }

    fun react(id: Long, emoji: Emoji, add: Boolean = true) {
        val messageId = messageMap.entries.find { id in it.value }?.key ?: return
        val userId = userMessageMap.entries.find { messageId in it.value }?.key ?: return
        plugin.penjamin?.openPrivateChannelById(userId)?.queue { channel ->
            channel.retrieveMessageById(messageId).queue { originalMessage ->
                if(add) originalMessage.addReaction(emoji).queue()
                else originalMessage.removeReaction(emoji).queue()
            }
        }
    }

    fun getSender(referenceId: Long): Long? {
        val messageId = messageMap.entries.find { referenceId in it.value }?.key ?: return null
        val senderId = userMessageMap.entries.find { messageId in it.value }?.key
        return senderId
    }

    fun forward(sender: Long, message: Message) {
        val finalMessage = message.contentRaw + message.attachments.joinToString("\n", prefix = "\n") { it.url }
        for (recipient in plugin.dmRecipients) {
            recipient.openPrivateChannel().queue { channel ->
                channel.sendMessage("**Anonymous says**\n$finalMessage").addEmbeds(message.embeds)
                    .addComponents(message.components).queue { forward ->
                    userMessageMap.getOrPut(sender) { mutableSetOf(message.idLong) }.add(message.idLong)
                    messageMap.getOrPut(message.idLong) { mutableSetOf(forward.idLong) }.add(forward.idLong)
                    message.addReaction(Emoji.fromUnicode("\uD83D\uDCE8")).queue()
                    for (forward in message.messageSnapshots) {
                        val finalForward = forward.contentRaw + forward.attachments.joinToString("\n", prefix = "\n") { it.url }
                        channel.sendMessage("> $finalForward").addEmbeds(forward.embeds)
                            .addComponents(forward.components).queue { forward ->
                            messageMap.getOrPut(message.idLong) { mutableSetOf(forward.idLong) }.add(forward.idLong)
                        }
                    }
                }
            }
        }
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val originalSenderId = getSender(event.messageIdLong) ?: return
        react(originalSenderId, event.emoji)
    }

    override fun onMessageReactionRemove(event: MessageReactionRemoveEvent) {
        val originalSenderId = getSender(event.messageIdLong) ?: return
        react(originalSenderId, event.emoji, add = false)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (plugin.settings.getBoolean("penjamin.enabled") == false) return
        if (event.channelType != ChannelType.PRIVATE || event.author.isBot) return

        val sender = event.author.idLong
        if (sender in plugin.settings.getLongList("penjamin.blocked")) return

        val message = event.message

        if (event.author in plugin.dmRecipients) {
            event.message.referencedMessage?.idLong?.let {
                val originalSenderId = getSender(it) ?: return
                when (event.message.contentRaw) {
                    "!block" -> {
                        plugin.settings.append("penjamin.blocked", originalSenderId)
                        message.addReaction(Emoji.fromUnicode("✅")).queue()
                    }
                    "!unblock" -> {
                        plugin.settings.remove("penjamin.blocked", originalSenderId)
                        message.addReaction(Emoji.fromUnicode("✅")).queue()
                    }
                    "!premium" -> {
                        plugin.settings.append("penjamin.premium", originalSenderId)
                        message.addReaction(Emoji.fromUnicode("✅")).queue()
                    }
                    "!unpremium" -> {
                        plugin.settings.remove("penjamin.premium", originalSenderId)
                        message.addReaction(Emoji.fromUnicode("✅")).queue()
                    }
                    else -> reply(it, message)
                }
                return
            }
        }

        if(sender !in plugin.settings.getLongList("penjamin.premium")) {
            val size = userMessageMap[sender]?.size ?: 1
            if (size > limit) {
                message.addReaction(Emoji.fromUnicode("❌")).queue()
                message.reply("Try again tomorrow!").queue()
                return
            }
        }

        forward(sender, message)

    }
}