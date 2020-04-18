package me.zeroeightsix.waypoints.api;

import net.minecraft.util.math.Vec2f;

public final class RenderWaypoint {

    public final Waypoint waypoint;
    public final Vec2f screenPos;

    public RenderWaypoint(Waypoint waypoint, Vec2f screenPos) {
        this.waypoint = waypoint;
        this.screenPos = screenPos;
    }

}
