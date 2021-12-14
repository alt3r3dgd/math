@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package me.altered.math

import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.pow

class Matrix(content: List<List<Double>>) : Collection<Double> {
    private val content: List<List<Double>>

    /** The width of the matrix. */
    val width get() = if (height == 0) 0 else content[0].size
    /** The height of the matrix. */
    val height get() = content.size

    /** Whether the matrix width and height are equal. */
    val isSquare get() = width == height
    /** Whether the matrix contains only zeros above or below the main diagonal. */
    val isTriangle get() = isZeroBelowMain || isZeroAboveMain
    /** Whether the matrix contains only zeros above and below the main diagonal. */
    val isDiagonal get() = isZeroBelowMain && isZeroAboveMain
    /** Whether the matrix has a non-zero determinant. */
    val isDegenerate get() = determinant == 0.0
    /** Whether the matrix contains only zeros below the main diagonal. */
    val isZeroBelowMain get(): Boolean {
        for (j in 0 until height)
            for (i in 0 until j)
                if (this[i, j] != 0.0)
                    return false
        return true
    }
    /** Whether the matrix contains only zeros above the main diagonal. */
    val isZeroAboveMain get(): Boolean {
        for (i in 0 until width)
            for (j in 0 until i)
                if (this[i, j] != 0.0)
                    return false
        return true
    }

    /** The main diagonal of the matrix, or `a(i, j)` where `i = j`. */
    val mainDiagonal get() = content.mapIndexed { j, row -> row[j] }
    /** The side diagonal of the matrix, or `a(i, j)` where `i = width - j`. */
    val sideDiagonal get() = content.mapIndexed { j, row -> row[width - j - 1] }

    /** A matrix that contains all of the elements of the original matrix with swapped i and j coordinates. */
    val transposed get() = Matrix(width, height) { i, j -> this[j, i] }
    /** A matrix that is made of complements of each element in the original matrix. */
    val adjunct get() = Matrix(height, width) { i, j -> getComplement(i, j) }
    /** Such matrix with which the equality `original * this = identity` is true. */
    val reversed get() = if (isSquare && !isDegenerate) adjunct.transposed / determinant else null

    /** The determinant of the matrix. */
    val determinant get(): Double = when {
        !isSquare -> Double.NaN
        width == 1 -> this[0, 0]
        width == 2 -> this[0, 0] * this[1, 1] - this[1, 0] * this[0, 1]
        width == 3 -> this[0, 0] * this[1, 1] * this[2, 2] +
                this[0, 1] * this[1, 2] * this[2, 0] +
                this[1, 0] * this[2, 1] * this[0, 2] -
                this[2, 0] * this[1, 1] * this[0, 2] -
                this[2, 1] * this[1, 2] * this[0, 0] -
                this[1, 0] * this[0, 1] * this[2, 2]
        else -> (0 until width).sumOf { this[it, 0] * getComplement(it, 0) }
    }
    /** why would we need this shit */
    val trace get() = if (isSquare) (0 until width).sumOf { this[it, it] } else Double.NaN

    init {
        val maxWidth = content.maxOf(List<Double>::size)
        this.content = List(content.size) {
            content[it].run {
                if (size == maxWidth) this
                else this + List(maxWidth - size) { 0.0 }
            }
        }
    }

    operator fun get(i: Int, j: Int) = content[i][j]
    operator fun get(w: IntRange, h: IntRange) =
        Matrix(h.last - h.first, w.last - w.first) { i, j -> this[i + h.first, j + w.first] }

    operator fun plus(other: Matrix) = Matrix(
        max(height, other.height),
        max(width, other.width)
    ) { i, j -> getOrZero(i, j) + other.getOrZero(i, j) }

    operator fun minus(other: Matrix) = Matrix(
        max(height, other.height),
        max(width, other.width)
    ) { i, j -> getOrZero(i, j) - other.getOrZero(i, j) }

    operator fun times(other: Matrix) = if (width == other.height)
        Matrix(height, other.width) { i, j ->
            (0 until width).sumOf { this[i, it] * other[it, j] }
        }
    else null

    operator fun times(number: Number) = map(number.toDouble()::times)
    operator fun div(number: Number) = map(number.toDouble()::revDiv)
    operator fun rem(number: Number) = map(number.toDouble()::revRem)
    operator fun unaryMinus() = map(Double::unaryMinus)

    /** @return The element at [i], [j], if exists, else zero. */
    fun getOrZero(i: Int, j: Int) = if (i in 0 until width && j in 0 until height) this[i, j] else 0.0
    /** @return The element at [i], [j], if exists, else null. */
    fun getOrNull(i: Int, j: Int) = if (i in 0 until width && j in 0 until height) this[i, j] else null

    /** @return A `List` of numbers in the [i]-th row. */
    fun getRow(i: Int) = content[i]
    /** @return A `List` of numbers in the [j]-th column. */
    fun getColumn(j: Int) = List(height) { this[it, j] }

    /** @return A copy of the matrix where the [i]-th row and [j]-th column are not present. */
    fun getSubmatrix(i: Int, j: Int) = Matrix(height - 1, width - 1) { _i, _j ->
        this[if (_i >= i) _i + 1 else _i, if (_j >= j) _j + 1 else _j]
    }
    /** @return The minor of the matrix at [i], [j], or the determinant of the submatrix of the matrix at [i], [j]. */
    fun getMinor(i: Int, j: Int) = getSubmatrix(i, j).determinant
    /** @return The minor of the matrix at [i], [j] multiplied by -1 to the power of the sum of the indexes. */
    fun getComplement(i: Int, j: Int) = (-1.0).pow(i + j) * getMinor(i, j)

    inline fun <T : Number> map(transform: (Double) -> T) =
        Matrix(mapToList { num -> transform(num).toDouble() })

    inline fun <T : Number> mapIndexed(transform: (i: Int, j: Int, number: Double) -> T): Matrix =
        Matrix(mapToListIndexed { i, j, num -> transform(i, j, num).toDouble() })

    inline fun <T> mapToList(transform: (Double) -> T) =
        toList().map { it.map(transform) }

    inline fun <T> mapToListIndexed(transform: (i: Int, j: Int, number: Double) -> T) =
        toList().mapIndexed { i, l -> l.mapIndexed { j, num -> transform(i, j, num) } }

    inline fun <T> flatMap(transform: (Double) -> T) =
        List(width * height) { transform(this[it / width, it % width]) }

    inline fun <T> flatMapIndexed(transform: (i: Int, j: Int, number: Double) -> T) =
        List(width * height) { transform(it / width, it % width, this[it / width, it % width]) }

    fun toList(): List<List<Double>> = content.toMutableList()
    fun flatten(): List<Double> = toList().flatten()

    override val size get() = width * height
    override fun iterator() = MatrixIterator(this)
    override fun isEmpty() = width == 0 || height == 0
    override fun contains(element: Double) = content.any { row -> row.any { it == element } }
    override fun containsAll(elements: Collection<Double>) = elements.all(::contains)

    override fun equals(other: Any?): Boolean {
        if (other !is Matrix || width != other.width || height != other.height)
            return false
        for (i in 0 until width)
            for (j in 0 until height)
                if (this[i, j] != other[i, j])
                    return false
        return true
    }

    override fun hashCode() = 31 * (31 * content.hashCode() + width) + height

    override fun toString() = if (height > 0) Array(height) { StringBuilder("⎜") }.apply {
        for (j in 0 until width) {
            for (i in 0 until height)
                this[i].append(' ', get(i, j))
            val maxLen = maxOf(StringBuilder::length)
            for (i in 0 until height)
                this[i].append(" ".repeat(maxLen - this[i].length))
        }
        if (height == 1)
            this[0].append(" )")[0] = '('
        else {
            this[0].append(" ⎞")[0] = '⎛'
            for (i in 1..height - 2)
                this[i].append(" ⎟")
            this[height - 1].append(" ⎠")[0] = '⎝'
        }
    }.joinToString("\n")
    else "()"

    companion object {
        /**
         * @param numbers The numbers (top to bottom) of the column.
         * @return A 1-wide matrix of [numbers].
         */
        @JvmStatic
        fun column(vararg numbers: Number) = Matrix(numbers.size, 1) { i, _ -> numbers[i] }
        /**
         * @param height The desired height of the matrix.
         * @param formula The function that defines the numbers of the column via zero-based indexes.
         * @return A [height]x1 matrix of numbers defined by [formula].
         */
        @JvmStatic
        fun column(height: Int, formula: (Int) -> Number) = Matrix(height, 1) { i, _ -> formula(i) }

        /**
         * @param numbers The numbers (left to right) of the row.
         * @return A 1-high matrix of [numbers].
         */
        @JvmStatic
        fun row(vararg numbers: Number) = matrixOf(numbers.map(Number::toDouble))
        /**
         * @param width The desired width of the matrix.
         * @param formula The function that defines the numbers of the row via zero-based indexes.
         * @return A 1x[width] matrix of numbers defined by [formula].
         */
        @JvmStatic
        fun row(width: Int, formula: (Int) -> Number) = matrixOf(List(width) { formula(it).toDouble() })

        /**
         * @param numbers The numbers (top-left to bottom-right) of the diagonal.
         * @return A square matrix where every element of the main diagonal is set to a [numbers] element.
         */
        @JvmStatic
        fun diagonal(vararg numbers: Number) =
            Matrix(numbers.size, numbers.size) { i, j -> if (i == j) numbers[i] else 0.0 }
        /**
         * @param size The desired width and height of the matrix.
         * @param formula The function that defines the numbers of the diagonal via zero-based indexes.
         * @return A square matrix where every element of the main diagonal is defined by [formula].
         */
        @JvmStatic
        fun diagonal(size: Int, formula: (Int) -> Number) =
            Matrix(size, size) { i, j -> if (i == j) formula(i) else 0.0 }

        @JvmStatic
        operator fun Number.times(other: Matrix) = other * this
    }
}
