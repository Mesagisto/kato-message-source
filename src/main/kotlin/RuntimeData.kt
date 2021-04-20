package org.meowcat.mesagisto.bukkit

import io.nats.client.Subscription
import java.util.concurrent.ConcurrentHashMap

val finos = ConcurrentHashMap<String, Subscription>()
