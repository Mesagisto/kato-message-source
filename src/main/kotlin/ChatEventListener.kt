package org.meowcat.mesagisto.bukkit

import io.izzel.taboolib.module.locale.TLocale
import io.nats.client.Nats
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.bukkit.MesagistoPlugin.channel
import org.meowcat.mesagisto.bukkit.MesagistoPlugin.enable
import org.meowcat.mesagisto.bukkit.MesagistoPlugin.nats
import org.meowcat.mesagisto.bukkit.extension.launch
import kotlin.coroutines.CoroutineContext

object ChatEventListener : Listener, CoroutineScope {
   private val nc by lazy { Nats.connect(nats) }
   private val dispatcher by lazy { nc.createDispatcher() }

   private val cid by lazy { nc.serverInfo.clientId.toString() }
   private val natsHeaders by lazy { Headers().add("cid", cid) }

   @EventHandler
   suspend fun handle(event: AsyncPlayerChatEvent) {
      if (!enable) {
         TLocale.sendToConsole("warn.not-enable")
         return
      }
      if (nats == null || channel == "") {
         TLocale.sendToConsole("warn.incomplete-config")
         return
      }
      event.handle()
   }
   @JvmName("handle-chat-event")
   suspend fun AsyncPlayerChatEvent.handle() {
      val content = "<${player.name}>: $message".toByteArray()
      val mesage = NatsMessage(channel, null, natsHeaders, content, false)
      nc.publish(mesage)
      fino
   }
   private val fino by lazy {
      dispatcher.subscribe(channel) {
         if (it.headers["cid"].contains(cid)) return@subscribe
         launch run@{
            Bukkit.broadcastMessage(String(it.data))
         }
      }
   }
   override val coroutineContext: CoroutineContext
      get() = GlobalScope.coroutineContext
}
