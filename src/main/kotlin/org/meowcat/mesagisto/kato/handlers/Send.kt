package org.meowcat.mesagisto.kato.handlers

import arrow.core.left
import io.nats.client.impl.NatsMessage
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.client.Logger
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.Message
import org.meowcat.mesagisto.client.data.MessageType
import org.meowcat.mesagisto.client.data.Packet
import org.meowcat.mesagisto.client.data.Profile
import org.meowcat.mesagisto.client.toByteArray
import org.meowcat.mesagisto.kato.Config
import org.meowcat.mesagisto.kato.IdGen

suspend fun send(
  event: AsyncPlayerChatEvent
) {
  Logger.info("sending2")

  val channel = Config.channel ?: return
  val msgId = IdGen.gen()
  val chain = listOf<MessageType>(
    MessageType.Text(event.message)
  )
  val sender = event.player
  val message = Message(
    profile = Profile(
      // fixme
      0L,
      sender.name,
      sender.displayName
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = if (Config.cipher.enable) {
    Packet.encryptFrom(message.left())
  } else {
    Packet.from(message.left())
  }
  Server.sendAndRegisterReceive(0L, channel, packet) receive@{ it, _ ->
    return@receive receive(it as NatsMessage)
  }
}
