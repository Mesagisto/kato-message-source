package org.meowcat.mesagisto.bukkit

import org.bukkit.Bukkit
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.platform.BukkitPlugin

object MesagistoPlugin : Plugin() {

   val enable by lazy { CONFIG.getBoolean("enable") }
   val nats: String by lazy { CONFIG.getString("nats") }
   val channel by lazy { CONFIG.getString("channel", "")!! }

   private val bukkitPlugin by lazy { BukkitPlugin.getInstance() }

   @Config("config.yml", migrate = true)
   lateinit var CONFIG: SecuredFile
      private set

   override fun onLoad() {
      info("")
      info("信使插件加载中...${Bukkit.getBukkitVersion()}")
      info("")
   }

   override fun onEnable() {
      info("信使插件已启用. 当前版本 ${bukkitPlugin.description.version}")
      bukkitPlugin.server.pluginManager.registerEvents(ChatEventListener, bukkitPlugin)
   }
}
