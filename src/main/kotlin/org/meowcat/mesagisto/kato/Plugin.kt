package org.meowcat.mesagisto.kato

import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.java.JavaPlugin
import org.meowcat.mesagisto.client.* // ktlint-disable no-wildcard-imports
import org.meowcat.mesagisto.kato.handlers.Listener
import org.meowcat.mesagisto.kato.platform.JvmPlugin
import kotlin.coroutines.EmptyCoroutineContext

object Plugin : JvmPlugin(), CoroutineScope {

  override val coroutineContext = EmptyCoroutineContext

  private lateinit var bukkit: JavaPlugin

  private var closed: Boolean = false

  override suspend fun onLoad(bukkit: JavaPlugin): Result<Unit> = runCatching fn@{
    this.bukkit = bukkit
    Logger.bridgeToBukkit(Plugin.bukkit.logger)
    Config.init(bukkit.config)
    return@fn
  }
  override suspend fun onEnable() = runCatching fn@{
    if (closed) {
      throw IllegalStateException("hot reload error")
    }
    if (!Config.enable) {
      Logger.info { "Mesagisto信使未启用" }
      return@fn
    }
    MesagistoConfig.builder {
      name = "bukkit"
      natsAddress = Config.nats.address
      cipherEnable = Config.cipher.enable
      cipherKey = Config.cipher.key
      cipherRefusePlain = Config.cipher.refusePlain
    }.apply()

    bukkit.server.pluginManager.registerEvents(Listener, bukkit)
    Logger.info { "Mesagisto信使启用成功" }
  }

  override suspend fun onDisable() = runCatching {
    IdGen.save()
    Config.save()
    Server.close()
    closed = true
  }
}
