package co.akoot.plugins.alleycat.listeners

import co.akoot.plugins.alleycat.AlleyCat
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PenjaminListener(private val plugin: AlleyCat): ListenerAdapter() {
    // todo: improve
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if(event.channelType != ChannelType.PRIVATE || event.author.isBot) return
        val message = event.message
        for(recipient in plugin.dmRecipients) {
            recipient.openPrivateChannel().queue {
                it.sendMessage("**Anonymous says:**\n${message.contentRaw}").queue {
                    message.reply(setOf("Message received!", "Got it!", "Thanks!").random()).queue()
                }
                for(attachment in message.attachments) {
                    it.sendMessage(attachment.url).queue()
                }
                if(!message.embeds.isEmpty()) {
                    it.sendMessageEmbeds(message.embeds).queue()
                }
                if(!message.components.isEmpty()) {
                    it.sendMessageComponents(message.components).queue()
                }
                for(forward in message.messageSnapshots) {
                    it.sendMessage("> ${forward.contentRaw}").queue()
                    for(attachment in forward.attachments) {
                        it.sendMessage("> ${attachment.url}").queue()
                    }
                    if(!forward.embeds.isEmpty()) {
                        it.sendMessageEmbeds(forward.embeds).queue()
                    }
                    if(!forward.components.isEmpty()) {
                        it.sendMessageComponents(forward.components).queue()
                    }
                }
            }
        }
    }
}