package org.meowcat.mesagisto.kato.platform

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import org.bukkit.plugin.java.JavaPlugin
import org.meowcat.mesagisto.kato.Plugin
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class Bukkit : JavaPlugin(), CoroutineScope {
  override val coroutineContext: CoroutineContext = EmptyCoroutineContext

  private val inner: JvmPlugin by lazy { Plugin }
  override fun onLoad() = runBlocking {
    coroutineContext.ensureActive()
    inner.coroutineContext.ensureActive()
    inner.onLoad(this@Bukkit).getOrThrow()
  }
  override fun onEnable(): Unit = runBlocking {
    inner.launch {
      inner.onEnable().getOrThrow()
    }
  }

  override fun onDisable() = runBlocking {
    inner.launch {
      inner.onDisable().getOrThrow()
    }
    inner.coroutineContext.cancel()
    coroutineContext.cancel()
  }
}
