package org.meowcat.mesagisto.kato.handlers

import arrow.core.Either
import io.nats.client.impl.NatsMessage
import org.bukkit.Bukkit
import org.meowcat.mesagisto.client.Base64
import org.meowcat.mesagisto.client.data.Message
import org.meowcat.mesagisto.client.data.MessageType
import org.meowcat.mesagisto.client.data.Packet

suspend fun receive(
  message: NatsMessage
): Result<Unit> = runCatching {
  when (val packet = Packet.fromCbor(message.data).getOrThrow()) {
    is Either.Left -> {
      receiveMessage(packet.value).getOrThrow()
    }
    is Either.Right -> {
      packet.value
    }
  }
}
fun receiveMessage(
  message: Message,
): Result<Unit> = runCatching fn@{
  val senderName = with(message.profile) { nick ?: username ?: Base64.encodeToString(id) }
  val msgList = message.chain.filterIsInstance<MessageType.Text>()
  msgList.forEach {
    val text = "<$senderName> ${it.content}"
    Bukkit.broadcastMessage(text)
  }
}
