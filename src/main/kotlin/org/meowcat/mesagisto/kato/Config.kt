package org.meowcat.mesagisto.kato

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class RootConfig(
  val enable: Boolean = false,
  val channel: String = "your-channel",
  val nats: String = "nats://itsusinn.site:4222",
  val cipher: CipherConfig = CipherConfig(),
  var idBase: Int = 0
)
@Serializable
data class CipherConfig(
  val enable: Boolean = true,
  val key: String = "default-key",
  @SerialName("refuse-plain")
  val refusePlain: Boolean = true
)
