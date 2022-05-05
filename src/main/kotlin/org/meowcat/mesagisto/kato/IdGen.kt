package org.meowcat.mesagisto.kato

import org.meowcat.mesagisto.kato.Plugin.CONFIG
import java.util.concurrent.atomic.AtomicInteger

object IdGen {
  private val cur by lazy { AtomicInteger(CONFIG.idBase) }
  fun gen(): Int = cur.getAndIncrement()

  fun save() {
    CONFIG.idBase = cur.get()
  }
}
