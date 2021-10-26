package org.meowcat.mesagisto.kato.handlers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.client.Logger
import org.meowcat.mesagisto.kato.Config
import org.meowcat.mesagisto.kato.Plugin
import kotlin.coroutines.CoroutineContext

object Listener : Listener, CoroutineScope {

  @EventHandler
  fun handle(event: AsyncPlayerChatEvent) {
    if (!Config.enable) {
      Logger.info("插件未被启用！")
      return
    }
    Logger.info("sending")

    launch {
      send(event)
    }
  }

  override val coroutineContext: CoroutineContext
    get() = Plugin.coroutineContext
}
