package org.meowcat.mesagisto.kato

import org.bukkit.configuration.file.FileConfiguration
import java.io.File

object Config {
  private lateinit var inner: FileConfiguration
  private val file = File("plugins/mesagisto/config.yml")
  val enable: Boolean
    get() = inner.getBoolean("enable", false)
  val channel: String?
    get() = inner.getString("channel")
  var idBase: Int
    get() = inner.getInt("id-base")
    set(value) = inner.set("id-base", value)

  val nats by lazy { NatsConfig() }
  val cipher by lazy { CipherConfig() }

  fun init(c: FileConfiguration) {
    if (!file.exists()) {
      File(file.parent).mkdirs()
      file.createNewFile()
      file.writeText(
        """
        ---
        enable: false
        channel: "default"
        id-base: 0
        nats:
          address:  "nats://itsusinn.site:4222"
        cipher:
          enable: true
          key: "this is an example key"
          refuse-plain: true
        """.trimIndent()
      )
    }
    c.load(file)
    c.save(file)
    inner = c
  }
  fun save() {
    inner.save(File("plugins/mesagisto/config.yml"))
  }
  class NatsConfig {
    val address: String
      get() = inner.getString("nats.server", "nats://itsusinn.site:4222")!!
  }

  class CipherConfig {
    val enable: Boolean
      get() = inner.getBoolean("cipher.enable", true)
    val key: String
      get() = inner.getString("cipher.key", "an example key")!!
    val refusePlain: Boolean
      get() = inner.getBoolean("cipher.refuse-plain", true)
  }
}
