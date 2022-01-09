package org.meowcat.mesagisto.kato.handlers

import io.nats.client.impl.NatsMessage
import kotlinx.serialization.* // ktlint-disable no-wildcard-imports
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.* // ktlint-disable no-wildcard-imports
import org.meowcat.mesagisto.client.toByteArray
import org.meowcat.mesagisto.kato.Config
import org.meowcat.mesagisto.kato.IdGen

@OptIn(ExperimentalStdlibApi::class, InternalSerializationApi::class)
suspend fun send(
  event: AsyncPlayerChatEvent
) {
  val channel = Config.channel ?: return
  val msgId = IdGen.gen()
  val chain = listOf<MessageType>(
    MessageType.Text(event.message)
  )
  val sender = event.player
  val message = Message(
    profile = Profile(
      // fixme
      ByteArray(0),
      sender.name,
      sender.displayName
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())

  Server.sendAndRegisterReceive(0L, channel, packet) receive@{ it, _ ->
    return@receive receive(it as NatsMessage)
  }
}
