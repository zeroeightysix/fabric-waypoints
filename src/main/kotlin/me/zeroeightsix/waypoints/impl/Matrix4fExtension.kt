package me.zeroeightsix.waypoints.impl

import me.zeroeightsix.waypoints.mixin.IMatrix4f
import net.minecraft.client.util.math.Matrix4f
import net.minecraft.util.math.Quaternion

val Matrix4f.a00
    get() = (this as IMatrix4f).a00
val Matrix4f.a01
    get() = (this as IMatrix4f).a01
val Matrix4f.a02
    get() = (this as IMatrix4f).a02
val Matrix4f.a03
    get() = (this as IMatrix4f).a03
val Matrix4f.a10
    get() = (this as IMatrix4f).a10
val Matrix4f.a11
    get() = (this as IMatrix4f).a11
val Matrix4f.a12
    get() = (this as IMatrix4f).a12
val Matrix4f.a13
    get() = (this as IMatrix4f).a13
val Matrix4f.a20
    get() = (this as IMatrix4f).a20
val Matrix4f.a21
    get() = (this as IMatrix4f).a21
val Matrix4f.a22
    get() = (this as IMatrix4f).a22
val Matrix4f.a23
    get() = (this as IMatrix4f).a23
val Matrix4f.a30
    get() = (this as IMatrix4f).a30
val Matrix4f.a31
    get() = (this as IMatrix4f).a31
val Matrix4f.a32
    get() = (this as IMatrix4f).a32
val Matrix4f.a33
    get() = (this as IMatrix4f).a33

fun Matrix4f.multiplyMatrix(q: Quaternion) = Quaternion(
    a00 * q.b + a01 * q.c + a02 * q.d + a03 * q.a,
    a10 * q.b + a11 * q.c + a12 * q.d + a13 * q.a,
    a20 * q.b + a21 * q.c + a22 * q.d + a23 * q.a,
    a30 * q.b + a31 * q.c + a32 * q.d + a33 * q.a
)

fun Quaternion.toScreen(): Quaternion {
    val newA = 1.0f / a * 0.5f
    return Quaternion(
        b * newA + 0.5f,
        c * newA + 0.5f,
        d * newA + 0.5f,
        newA
    )
}