package me.zeroeightsix.waypoints.impl

import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

@Suppress("unused")
fun init() {

    Registry.register(Registry.REGISTRIES, WaypointRegistry.identifier, WaypointRegistry)

}

