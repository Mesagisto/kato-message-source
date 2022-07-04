package org.meowcat.mesagisto.kato.handlers

import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.* // ktlint-disable no-wildcard-imports
import org.meowcat.mesagisto.client.toByteArray
import org.meowcat.mesagisto.kato.IdGen
import org.meowcat.mesagisto.kato.Plugin.CONFIG
import org.meowcat.mesagisto.kato.asBytes

suspend fun send(
  event: AsyncPlayerChatEvent
) {
  val channel = CONFIG.channel
  val msgId = IdGen.gen()
  val chain = listOf<MessageType>(
    MessageType.Text(event.message)
  )
  val sender = event.player
  val message = Message(
    profile = Profile(
      sender.uniqueId.asBytes(),
      sender.name,
      sender.playerListName
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())
  Server.send("0", channel, packet)
}
