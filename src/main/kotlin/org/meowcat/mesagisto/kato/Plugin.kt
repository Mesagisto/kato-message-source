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
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

object Plugin : JvmPlugin(), CoroutineScope {
  private lateinit var bukkit: JavaPlugin

  private var closed: Boolean = false

  private val CONFIG_KEEPER = ConfigKeeper.create(File("plugins/mesagisto/config.yml").toPath()) { RootConfig() }
  private val DATA_KEEPER = ConfigKeeper.create(File("plugins/mesagisto/data.yml").toPath()) { RootData() }
  val CONFIG = CONFIG_KEEPER.value
  val DATA = DATA_KEEPER.value
  override suspend fun onLoad(bukkit: JavaPlugin): Result<Unit> = runCatching fn@{
    Path("db_v2/bukkit").createDirectories()
    this.bukkit = bukkit
    Logger.bridgeToBukkit(Plugin.bukkit.logger)
    CONFIG_KEEPER.save()
    Template.init()
    return@fn
  }
  override suspend fun onEnable() = runCatching fn@{
    if (closed) {
      throw IllegalStateException("hot reload error")
    }

    MesagistoConfig.builder {
      name = "bukkit"
      natsAddress = CONFIG.nats
      cipherKey = CONFIG.cipher.key
    }.apply()
    Receive.recover()
    bukkit.server.pluginManager.registerEvents(Listener, bukkit)
    Logger.info { "Mesagisto信使启用成功" }
  }

  override suspend fun onDisable() = runCatching {
    // attention!! before this term, zip(jar) is closed
    // so, loading class before onDisable
    DATA_KEEPER.save()
    Server.close()
    closed = true
  }
}
