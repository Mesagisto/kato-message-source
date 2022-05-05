package org.meowcat.mesagisto.kato

import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.java.JavaPlugin
import org.meowcat.mesagisto.client.Logger
import org.meowcat.mesagisto.client.MesagistoConfig
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.utils.ConfigKeeper
import org.meowcat.mesagisto.kato.handlers.Listener
import org.meowcat.mesagisto.kato.handlers.Receive
import org.meowcat.mesagisto.kato.platform.JvmPlugin
import java.nio.file.Path
import kotlin.coroutines.EmptyCoroutineContext

object Plugin : JvmPlugin(), CoroutineScope {

  override val coroutineContext = EmptyCoroutineContext

  private lateinit var bukkit: JavaPlugin

  private var closed: Boolean = false

  private val CONFIG_KEEPER = ConfigKeeper.create(Path.of("plugins/mesagisto/config.yml")) { RootConfig() }

  val CONFIG = CONFIG_KEEPER.value

  override suspend fun onLoad(bukkit: JavaPlugin): Result<Unit> = runCatching fn@{
    this.bukkit = bukkit
    Logger.bridgeToBukkit(Plugin.bukkit.logger)
    CONFIG_KEEPER.save()
    return@fn
  }
  override suspend fun onEnable() = runCatching fn@{
    if (closed) {
      throw IllegalStateException("hot reload error")
    }
    if (!CONFIG.enable) {
      Logger.info { "Mesagisto信使未启用" }
      return@fn
    }

    MesagistoConfig.builder {
      name = "bukkit"
      natsAddress = CONFIG.nats
      cipherEnable = CONFIG.cipher.enable
      cipherKey = CONFIG.cipher.key
      cipherRefusePlain = CONFIG.cipher.refusePlain
    }.apply()
    Receive.recover()
    bukkit.server.pluginManager.registerEvents(Listener, bukkit)
    Logger.info { "Mesagisto信使启用成功" }
  }

  override suspend fun onDisable() = runCatching {
    // attention!! before this term, zip(jar) is closed
    // so, loading class before onDisable
    IdGen.save()
    CONFIG_KEEPER.save()
    if (CONFIG.enable) {
      Server.close()
    }
    closed = true
  }
}
