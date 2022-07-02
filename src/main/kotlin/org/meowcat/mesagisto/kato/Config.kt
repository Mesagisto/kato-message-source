package org.meowcat.mesagisto.kato

import com.fasterxml.jackson.annotation.JsonAlias

data class RootConfig(
  val enable: Boolean = false,
  val channel: String = "your-channel",
  val cipher: CipherConfig = CipherConfig(),
  @JsonAlias("id_base")
  var idBase: Int = 0,
  val nats: String = "nats://nats.mesagisto.org:4222",
  val template: TemplateConfig = TemplateConfig()
)

data class CipherConfig(
  val key: String = "default",
)

data class TemplateConfig(
  val message: String = "§7<{{sender}}> {{content}}"
)
