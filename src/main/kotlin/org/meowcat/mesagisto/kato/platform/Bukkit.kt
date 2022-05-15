package org.meowcat.mesagisto.kato.platform

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import org.bukkit.plugin.java.JavaPlugin
import org.meowcat.mesagisto.kato.Plugin

class Bukkit : JavaPlugin(), CoroutineScope {
  override val coroutineContext = CoroutineScope(Dispatchers.Default).coroutineContext

  private val inner: JvmPlugin = Plugin
  override fun onLoad() {
    launch {
      coroutineContext.ensureActive()
      inner.coroutineContext.ensureActive()
      inner.onLoad(this@Bukkit).getOrThrow()
    }
  }
  override fun onEnable() {
    launch {
      inner.onEnable().getOrThrow()
    }
  }

  override fun onDisable() {
    runBlocking {
      inner.onDisable().getOrThrow()
    }
    runCatching {
      inner.coroutineContext.cancel()
      coroutineContext.cancel()
    }
  }
}
