package org.meowcat.mesagisto.kato

import java.util.concurrent.atomic.AtomicInteger

object IdGen {
  private val cur by lazy { AtomicInteger(Config.idBase) }
  fun gen(): Int = cur.getAndIncrement()

  fun save() {
    Config.idBase = cur.get()
  }
}
