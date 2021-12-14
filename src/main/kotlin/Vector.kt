@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package me.altered.math

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    var length
        get() = sqrt(sqrLength)
        set(value) {
            val dLen = value / length
            x *= dLen
            y *= dLen
            z *= dLen
        }
    val sqrLength get() = x * x + y * y + z * z
    val normal get() = this / length

    constructor() : this(0.0, 0.0, 0.0)
    constructor(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y, z - other.z)
    operator fun unaryMinus() = Vector(-x, -y, -z)
    operator fun unaryPlus() = this

    operator fun times(num: Number) = map(num.toDouble()::times)
    operator fun div(num: Number) = map(num.toDouble()::revDiv)
    operator fun rem(num: Number) = map(num.toDouble()::revRem)

    operator fun Number.times(p: Vector) = p.map(toDouble()::times)
    operator fun Number.div(p: Vector) = p.map(toDouble()::revDiv)
    operator fun Number.rem(p: Vector) = p.map(toDouble()::revRem)

    /**The dot product of vectors.*/
    operator fun times(other: Vector) = dot(other)

    operator fun compareTo(other: Vector) = length.compareTo(other.length)

    /**The dot product of vectors.*/
    infix fun dot(other: Vector) = x * other.x + y * other.y + z * other.z
    /**The cross product of vectors.*/
    infix fun cross(other: Vector) = Vector(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    /**The angle between vectors.*/
    infix fun angleTo(other: Vector) = acos(times(other) / (length * other.length))
    /**
     * Such vector that is a result of this vector bouncing of a wall with a normal of [other].
     * @param other must be normalized.
     */
    infix fun reflectFrom(other: Vector): Vector {
        val norm = other.normal
        return this - 2 * (this * norm) * norm
    }
    /**Tells whether the vectors are parallel to each other.*/
    infix fun isCollinearTo(other: Vector) = normal == other.normal || normal == -other.normal
    /**The distance between two vectors.*/
    infix fun distanceTo(other: Vector) = (other - this).length
    /**The direction from this vector to [other].*/
    infix fun directionTo(other: Vector) = (other - this).normal
    /**The projection of the vector onto an axis defined by [other].*/
    infix fun projectOn(other: Vector) = other * times(other) / other.sqrLength

    /**The **2D** vector rotated in a plane by [angle] radians.*/
    infix fun rotatedBy(angle: Double): Vector {
        val sin = sin(angle)
        val cos = cos(angle)
        return Vector((cos * x) - (sin * y), (sin * x) + (cos * y))
    }

    inline fun <T : Number> map(transform: (Double) -> T) = Vector(transform(x), transform(y), transform(z))

    override fun toString() = "($x, $y, $z)"
    override fun equals(other: Any?) = other is Vector && x == other.x && y == other.y && z == other.z
    override fun hashCode(): Int = 31 * (31 * x.hashCode() + y.hashCode()) + z.hashCode()

    companion object {
        @JvmStatic
        val zero get() = Vector()
        @JvmStatic
        val one get() = Vector(1.0, 1.0, 1.0)
        @JvmStatic
        val right get() = Vector(1.0)
        @JvmStatic
        val left get() = Vector(-1.0)
        @JvmStatic
        val up get() = Vector(0.0, 1.0)
        @JvmStatic
        val down get() = Vector(0.0, -1.0)
        @JvmStatic
        val forward get() = Vector(0.0, 0.0, 1.0)
        @JvmStatic
        val back get() = Vector(0.0, 0.0, -1.0)
        @JvmStatic
        val infinity get() = Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
    }
}