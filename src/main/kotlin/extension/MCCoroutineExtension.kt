package org.meowcat.mesagisto.bukkit.extension

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.launch
import com.github.shynixn.mccoroutine.launchAsync
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.bukkit.plugin.java.JavaPlugin
import org.meowcat.mesagisto.bukkit.MesagistoPlugin
import kotlin.coroutines.CoroutineContext

fun launch(f: suspend CoroutineScope.() -> Unit): Job {
   return JavaPlugin.getPlugin(MesagistoPlugin.plugin::class.java).launch(f)
}

fun launchAsync(f: suspend CoroutineScope.() -> Unit): Job {
   return JavaPlugin.getPlugin(MesagistoPlugin.plugin::class.java).launchAsync(f)
}

val Dispatchers.minecraft: CoroutineContext
   get() {
      return JavaPlugin.getPlugin(MesagistoPlugin.plugin::class.java).minecraftDispatcher
   }

val Dispatchers.async: CoroutineContext
   get() {
      return JavaPlugin.getPlugin(MesagistoPlugin.plugin::class.java).asyncDispatcher
   }
