package org.meowcat.mesagisto.kato.handlers

import com.github.jknack.handlebars.Context
import io.nats.client.impl.NatsMessage
import org.bukkit.Bukkit
import org.meowcat.mesagisto.client.Base64
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.Either
import org.meowcat.mesagisto.client.data.Message
import org.meowcat.mesagisto.client.data.MessageType
import org.meowcat.mesagisto.client.data.Packet
import org.meowcat.mesagisto.kato.Plugin.CONFIG
import org.meowcat.mesagisto.kato.Template

object Receive {
  suspend fun recover() {
    Server.recv("0", CONFIG.channel) handler@{ msg, _ ->
      return@handler mainHandler(msg as NatsMessage)
    }
  }
}
fun mainHandler(
  message: NatsMessage
): Result<Unit> = runCatching {
  when (val packet = Packet.fromCbor(message.data).getOrThrow()) {
    is Either.Left -> {
      leftSubHandler(packet.value).getOrThrow()
    }
    is Either.Right -> {
      packet.value
    }
  }
}
fun leftSubHandler(
  message: Message,
): Result<Unit> = runCatching fn@{
  val senderName = with(message.profile) { nick ?: username ?: Base64.encodeToString(id) }
  val msgList = message.chain.filterIsInstance<MessageType.Text>()
  msgList.forEach {
    val text = renderMessage(senderName, it.content)
    Bukkit.broadcastMessage(text)
  }
}

private fun renderMessage(sender: String, content: String): String {
  val module = HashMap<String, String>(2)
  module.apply {
    put("sender", sender)
    put("content", content)
  }
  val context = Context.newContext(module)
  return Template.apply("message", context)
}
