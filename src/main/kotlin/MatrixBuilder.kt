package me.altered.math

/**
 * This is a **very** dirty class as it only contains the append function :(
 */
class MatrixBuilder(private val width: Int) {
    private var currentRow = 0
    private var currentColumn = 0
    private val content = mutableListOf<MutableList<Double>>()

    /**
     * Adds the [number] to the end of the matrix.
     */
    fun append(number: Number) {
        if (currentColumn == 0) content.add(MutableList(width) { 0.0 })
        content[currentRow].add(number.toDouble())
        if (++currentColumn == width) {
            currentColumn = 0
            currentRow++
        }
    }

    /**
     * Adds the [numbers] to the end of the matrix.
     */
    fun append(vararg numbers: Number): Unit = numbers.forEach(::append)

    fun toMatrix() = Matrix(content)
}