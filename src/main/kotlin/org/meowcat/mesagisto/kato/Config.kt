package org.meowcat.mesagisto.kato

import org.bukkit.event.EventPriority
import java.util.concurrent.atomic.AtomicInteger

data class RootConfig(
  val serverName: String = "Server Name",
  val channel: String = "your-channel",
  val target: String = "target-name",
  val nats: String = "nats://nats.mesagisto.org:4222",
  val cipher: CipherConfig = CipherConfig(),
  val eventPriority: EventPriority = EventPriority.NORMAL,
  val switch: SwitchConfig = SwitchConfig(),
  val template: TemplateConfig = TemplateConfig()
)

data class CipherConfig(
  val key: String = "default"
)

data class TemplateConfig(
  val message: String = "§7<{{sender}}> {{content}}",
  val join: String = "{{player}}加入了服务器",
  val leave: String = "{{player}}离开了服务器",
  val death: String = "{{message}}"
)
data class SwitchConfig(
  val chat: Boolean = true,
  val join: Boolean = true,
  val leave: Boolean = true,
  val death: Boolean = true
)
data class RootData(
  var idCounter: AtomicInteger = AtomicInteger(0)
)
