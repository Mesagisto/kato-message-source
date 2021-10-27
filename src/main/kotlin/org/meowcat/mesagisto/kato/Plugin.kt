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

  override suspend fun onLoad(bukkit: JavaPlugin): Result<Unit> = runCatching fn@{
    this.bukkit = bukkit
    Logger.bridgeToBukkit(Plugin.bukkit.logger)
    Config.init(bukkit.config)
    return@fn
  }
  override suspend fun onEnable() = runCatching fn@{
    if (!Config.enable) {
      Logger.info { "信使插件未启用" }
      return@fn
    }
    if (Config.cipher.enable) {
      Cipher.init(Config.cipher.key, Config.cipher.refusePlain)
    } else {
      Cipher.deinit()
    }
    Db.init("mc")
    Server.initNC(Config.nats.address)
    Res.resolvePhotoUrl { _, _ ->
      Result.failure(IllegalStateException("Unreachable"))
    }
    bukkit.server.pluginManager.registerEvents(Listener, bukkit)
    Logger.info { "信使插件启用成功" }
  }

  override suspend fun onDisable() = runCatching {
    IdGen.save()
    Config.save()
  }
}
