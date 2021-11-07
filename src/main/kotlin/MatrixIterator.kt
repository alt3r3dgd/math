@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package me.altered.math

class MatrixIterator(private val matrix: Matrix) : Iterator<Double> {
    private var current = 0
    override fun hasNext() = current < matrix.height * matrix.width
    override fun next() = matrix[current / matrix.width, current++ % matrix.width]
}
