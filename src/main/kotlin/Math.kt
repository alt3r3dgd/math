@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package me.altered.math

import kotlin.math.*

fun sqrt(vec: Vector) = Vector(sqrt(vec.x), sqrt(vec.y), sqrt(vec.z))
fun ceil(vec: Vector) = Vector(ceil(vec.x), ceil(vec.y), ceil(vec.z))
fun floor(vec: Vector) = Vector(floor(vec.x), floor(vec.y), floor(vec.z))
fun round(vec: Vector) = Vector(round(vec.x), round(vec.y), round(vec.z))
fun abs(vec: Vector) = Vector(abs(vec.x), abs(vec.y), abs(vec.z))
fun sign(vec: Vector) = Vector(sign(vec.x), sign(vec.y), sign(vec.z))

fun max(a: Vector, b: Vector) = Vector(max(a.x, b.x), max(a.y, b.y), max(a.z, b.z))
fun min(a: Vector, b: Vector) = Vector(min(a.x, b.x), min(a.y, b.y), max(a.z, b.z))

fun rowOf(vararg numbers: Number) = numbers.map(Number::toDouble)
fun matrixOf(vararg rows: List<Double>) = Matrix(rows.toList())
fun Matrix(height: Int, width: Int) = Matrix(List(height) { List(width) { 0.0 } })

/**
 * Creates a matrix based on the [builderAction].
 *
 * The way it works is you tell it the desired [width] of the matrix
 * and can add as many numbers as you want with the `append(num: Number)`
 * function while the builder automatically shifts rows for you.
 */
inline fun buildMatrix(width: Int, builderAction: MatrixBuilder.() -> Unit) =
    MatrixBuilder(width).apply(builderAction).toMatrix()

inline fun Matrix(height: Int, width: Int, formula: (Int, Int) -> Number) =
    Matrix(List(height) { i -> List(width) { j -> formula(i, j).toDouble() } })

fun Double.revDiv(other: Double) = other / this
fun Double.revRem(other: Double) = other % this

fun main() {
    val a = 0.0..1.0
    println(a)
}