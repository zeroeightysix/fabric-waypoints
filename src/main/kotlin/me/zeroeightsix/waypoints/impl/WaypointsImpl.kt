package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.fiber.tree.ConfigBranch
import me.zeroeightsix.fiber.tree.ConfigTree
import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.Waypoints
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.registry.Registry
import java.util.function.BiConsumer

@Environment(EnvType.CLIENT)
object WaypointsImpl: Waypoints, ClientModInitializer {

    override fun onInitializeClient() {
        Registry.register(Registry.REGISTRIES, WaypointRegistry.identifier, WaypointRegistry)
    }

    override val config: ConfigBranch = ConfigTree.builder()
        .beginAggregateValue("waypoints", listOf<Waypoint>(), Waypoint::class.java)
        .withListener { _, _ ->  }
        .finishValue()
        .build()

}

