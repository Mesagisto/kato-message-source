package org.meowcat.mesagisto.bukkit

import io.nats.client.Nats
import io.nats.client.impl.Headers
import io.nats.client.impl.NatsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.meowcat.mesagisto.bukkit.MesagistoPlugin.channel
import org.meowcat.mesagisto.bukkit.MesagistoPlugin.enable
import org.meowcat.mesagisto.bukkit.MesagistoPlugin.nats
import taboolib.common.platform.info
import kotlin.coroutines.CoroutineContext

object ChatEventListener : Listener, CoroutineScope {
   private val nc by lazy { Nats.connect(nats) }
   private val dispatcher by lazy { nc.createDispatcher() }

   private val cid by lazy { nc.serverInfo.clientId.toString() }
   private val natsHeaders by lazy { Headers().add("cid", cid) }

   @EventHandler
   fun handle(event: AsyncPlayerChatEvent) {
      if (!enable) {
         info("插件将不会被启用！")
         return
      }
      if (channel == "") {
         info("内容不完整/格式不正确 的配置文件")
         return
      }
      event.handle()
   }
   @JvmName("handle-chat-event")
   fun AsyncPlayerChatEvent.handle() {
      val content = "<${player.name}>: $message".toByteArray()
      val mesage = NatsMessage(channel, null, natsHeaders, content, false)
      launch {
         nc.publish(mesage)
      }
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
