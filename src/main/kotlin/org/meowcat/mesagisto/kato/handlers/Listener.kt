package org.meowcat.mesagisto.kato.handlers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.client.Logger
import org.meowcat.mesagisto.kato.Plugin
import org.meowcat.mesagisto.kato.Plugin.CONFIG
import kotlin.coroutines.CoroutineContext

object Listener : Listener, CoroutineScope {

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  fun handle(event: AsyncPlayerChatEvent) {
    if (!CONFIG.enable) {
      Logger.info { "Mesagisto信使未被启用！" }
      return
    }
    if (event.isCancelled) return

    launch {
      send(event)
    }
  }

  override val coroutineContext: CoroutineContext
    get() = Plugin.coroutineContext
}
