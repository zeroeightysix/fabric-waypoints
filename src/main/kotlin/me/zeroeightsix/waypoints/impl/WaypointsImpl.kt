package me.zeroeightsix.waypoints.impl

import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigBranch
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror
import io.github.fablabsmc.fablabs.impl.fiber.serialization.FiberSerialization
import me.zeroeightsix.waypoints.api.WaypointRenderer
import me.zeroeightsix.waypoints.api.Waypoints
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import java.nio.file.Files
import java.nio.file.Paths

object WaypointsImpl: Waypoints {

    private val WAYPOINT_TYPE = ConfigTypes.makeMap(ConfigTypes.STRING, ConfigTypes.STRING).derive(WaypointImpl::class.java,
        {
            val position = it["pos"]!!.split(',').toVec3d()
            val name = it["name"]!!
            val world = it["world"]!!
            val server = it["server"]!!
            val renderer = it["renderer"]!!
            WaypointImpl(position, name, WaypointRendererRegistry[Identifier(renderer)], server, world)
        },
        {
            mutableMapOf(
                Pair("pos", "${it.position.x},${it.position.y},${it.position.z}"),
                Pair("name", it.name),
                Pair("world", it.world),
                Pair("server", it.server),
                Pair("renderer", it.renderer.identifier.toString())
            )
        })

    private val WAYPOINTS = ConfigTypes.makeList(WAYPOINT_TYPE)

    var waypointMirror: PropertyMirror<MutableList<WaypointImpl>> = PropertyMirror.create(WAYPOINTS)

    override var waypoints: MutableMap<WaypointRenderer, MutableList<WaypointImpl>> = HashMap()

    internal val config: ConfigBranch = ConfigTree.builder()
        .beginValue("waypoints", WAYPOINTS, mutableListOf())
        .withListener { _, _ -> }
        .finishValue { waypointMirror.mirror(it) }
        .build()

    override fun addWaypoint(waypoint: WaypointImpl) {
        waypointMirror.value = waypointMirror.value.toMutableList().also { it.add(waypoint) }
        save()
        refresh()
    }

    override fun removeWaypoint(waypoint: WaypointImpl): Boolean {
        var ret: Boolean
        waypointMirror.value = waypointMirror.value.toMutableList().also {
            ret = it.remove(waypoint)
            save()
            refresh()
        }
        return ret
    }

    internal fun save() {
        FiberSerialization.serialize(config, Files.newOutputStream(Paths.get("waypoints.json5")), JanksonValueSerializer(false))
    }

    internal fun refresh() {
        waypoints = waypointMirror.value.groupByTo(mutableMapOf()) { it.renderer }
    }

}

private fun List<String>.toVec3d(): Vec3d = Vec3d(this[0].toDouble(), this[1].toDouble(), this[2].toDouble())

