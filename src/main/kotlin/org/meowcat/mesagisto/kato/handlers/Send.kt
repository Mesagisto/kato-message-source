package org.meowcat.mesagisto.kato.handlers

import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.* // ktlint-disable no-wildcard-imports
import org.meowcat.mesagisto.client.toByteArray
import org.meowcat.mesagisto.kato.Plugin.CONFIG
import org.meowcat.mesagisto.kato.Plugin.DATA
import org.meowcat.mesagisto.kato.Template
import org.meowcat.mesagisto.kato.asBytes
import org.meowcat.mesagisto.kato.stripColor

suspend fun send(
  event: AsyncPlayerChatEvent
) {
  val channel = CONFIG.channel
  val msgId = DATA.idCounter.getAndIncrement()
  val chain = listOf<MessageType>(
    MessageType.Text(event.message.stripColor())
  )
  val sender = event.player
  val message = Message(
    profile = Profile(
      sender.uniqueId.asBytes(),
      sender.name.stripColor(),
      sender.playerListName.stripColor()
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())
  Server.send(CONFIG.target, channel, packet)
}
suspend fun sendPlayerJoin(event: PlayerJoinEvent) {
  val servername = CONFIG.serverName
  val channel = CONFIG.channel
  val msgId = DATA.idCounter.getAndIncrement()
  val chain = listOf<MessageType>(
    MessageType.Text(Template.renderJoin(event.player.playerListName.stripColor()))
  )
  val message = Message(
    profile = Profile(
      0L.toByteArray(),
      servername,
      null
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())
  Server.send(CONFIG.target, channel, packet)
}
suspend fun sendPlayerLeave(event: PlayerQuitEvent) {
  val servername = CONFIG.serverName
  val channel = CONFIG.channel
  val msgId = DATA.idCounter.getAndIncrement()
  val chain = listOf<MessageType>(
    MessageType.Text(Template.renderLeave(event.player.playerListName.stripColor()))
  )
  val message = Message(
    profile = Profile(
      0L.toByteArray(),
      servername,
      null
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())
  Server.send(CONFIG.target, channel, packet)
}
suspend fun sendPlayerDeath(event: PlayerDeathEvent) {
  val servername = CONFIG.serverName
  val channel = CONFIG.channel
  val msgId = DATA.idCounter.getAndIncrement()
  val chain = listOf<MessageType>(
    MessageType.Text(Template.renderDeath(event.entity.playerListName, event.deathMessage.stripColor()))
  )
  val message = Message(
    profile = Profile(
      0L.toByteArray(),
      servername,
      null
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())
  Server.send(CONFIG.target, channel, packet)
}
