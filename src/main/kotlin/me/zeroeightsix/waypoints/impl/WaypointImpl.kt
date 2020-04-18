package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.api.Waypoint
import me.zeroeightsix.waypoints.api.WaypointRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Vec3d

data class WaypointImpl(
    override var position: Vec3d,
    override var name: String,
    override val renderer: WaypointRenderer,
    override var server: String = currentServer(),
    override var world: String = currentWorld()
) : Waypoint

fun currentServer(): String = if (MinecraftClient.getInstance().isInSingleplayer)
    "singleplayer:" + MinecraftClient.getInstance().server!!.levelName
else
    "multiplayer:" + MinecraftClient.getInstance().currentServerEntry!!.address

fun currentWorld(): String = MinecraftClient.getInstance().world!!.dimension.type.toString()