@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package me.altered.math

import kotlin.math.*

class Quaternion(var x: Double, var y: Double, var z: Double, var w: Double) {
    var length
        get() = sqrt(sqrLength)
        set(value) {
            val len = length
            x = x / len * value
            y = y / len * value
            z = z / len * value
            w = w / len * value
        }
    val sqrLength get() = x * x + y * y + z * z + w * w
    val normal get() = this / length
    val euler: Vector
        get() {
            val sinrcosp = 2 * (w * x + y * z)
            val cosrsinp = 1 - 2 * (x * x + y * y)
            val sinp = 2 * (w * y - z * x)
            val sinycosp = 2 * (w * z + x * y)
            val cosysinp = 1 - 2 * (y * y + z * z)
            return Vector(
                atan2(sinrcosp, cosrsinp),
                if (abs(sinp) >= 1) PI * 0.5 * sign(sinp) else asin(sinp),
                atan2(sinycosp, cosysinp)
            )
        }

    constructor() : this(0.0, 0.0, 0.0, 0.0)
    constructor(x: Number, y: Number, z: Number, w: Number)
            : this(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())
    constructor(euler: Vector) : this() {
        val halfA1 = euler.y * 0.5
        val halfA2 = euler.x * 0.5
        val halfA3 = euler.z * 0.5
        val cosA1 = cos(halfA1)
        val sinA1 = sin(halfA1)
        val cosA2 = cos(halfA2)
        val sinA2 = sin(halfA2)
        val cosA3 = cos(halfA3)
        val sinA3 = sin(halfA3)
        x = sinA1 * cosA2 * sinA3 + cosA1 * sinA2 * cosA3
        y = sinA1 * cosA2 * cosA3 - cosA1 * sinA2 * sinA3
        z = cosA1 * cosA2 * sinA3 - sinA1 * sinA2 * cosA3
        w = sinA1 * sinA2 * sinA3 + cosA1 * cosA2 * cosA3
    }
    constructor(angle: Number, axis: Vector) : this(0.0, 0.0, 0.0, 0.0) {
        val dAngle = angle.toDouble()
        val sin = sin(dAngle / 2.0)
        val nAxis = axis.normal
        x = nAxis.x * sin
        y = nAxis.y * sin
        z = nAxis.z * sin
        w = cos(dAngle / 2.0)
    }

    operator fun plus(other: Quaternion) = Quaternion(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: Quaternion) = Quaternion(x - other.x, y - other.y, z - other.z, w - other.w)
    operator fun times(a: Number) = Quaternion(x * a.toDouble(), y * a.toDouble(), z * a.toDouble(), w * a.toDouble())
    operator fun div(a: Number) = Quaternion(x / a.toDouble(), y / a.toDouble(), z / a.toDouble(), w / a.toDouble())
    operator fun rem(a: Number) = Quaternion(x % a.toDouble(), y % a.toDouble(), z % a.toDouble(), w % a.toDouble())
    operator fun compareTo(other: Quaternion) = length.compareTo(other.length)
    operator fun unaryMinus() = Quaternion(-x, -y, -z, -w)

    operator fun times(other: Quaternion) = Quaternion(
        w * other.x + x * other.w + y * other.z - z * other.y,
        w * other.y + y * other.w + z * other.x - x * other.z,
        w * other.z + z * other.w + x * other.y - y * other.x,
        w * other.w - x * other.x - y * other.y - z * other.z
    )
    operator fun times(point: Vector): Vector {
        val x = x * 2.0
        val y = y * 2.0
        val z = z * 2.0
        val xx = this.x * x
        val yy = this.y * y
        val zz = this.z * z
        val xy = this.x * y
        val xz = this.x * z
        val yz = this.y * z
        val wx = w * x
        val wy = w * y
        val wz = w * z
        return Vector(
            (1.0 - (yy + zz)) * point.x + (xy - wz) * point.y + (xz + wy),
            (xy + wz) * point.x + (1.0 - (xx + zz)) * point.y + (yz - wx),
            (xz - wy) * point.x + (yz + wx) * point.y + (1.0 - (xx + yy))
        ) * point.z
    }

    infix fun dot(other: Quaternion) = x * other.x + y * other.y + z * other.z + w * other.w

    /** The angle between this quaternion and [other] in **radians**. */
    infix fun angleTo(other: Quaternion): Double {
        val dot = dot(other)
        return if (dot > 1.0) 0.0 else acos(min(abs(dot), 1.0)) * 2.0
    }
    /** The angle between this quaternion and [other] in **radians**. */
    infix fun v(other: Quaternion) = angleTo(other)

    override fun toString() = "($x, $y, $z, $w)"
    override fun equals(other: Any?) = other is Quaternion && dot(other) > 1 // what the hell
    override fun hashCode() = 31 * (31 * (31 * x.hashCode() + y.hashCode()) + z.hashCode()) + w.hashCode()

    companion object {
        @JvmStatic
        val zero get() = Quaternion(0.0, 0.0, 0.0, 0.0)
        @JvmStatic
        val one get() = Quaternion(1.0, 1.0, 1.0, 1.0)
        @JvmStatic
        val identity get() = Quaternion(0.0, 0.0, 0.0, 1.0)
        @JvmStatic
        val infinity get() = Double.POSITIVE_INFINITY.let { Quaternion(it, it, it, it) }
    }
}