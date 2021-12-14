package exp

import me.altered.math.Matrix
import me.altered.math.matrixOf
import me.altered.math.rowOf
import org.junit.Test

internal class MatrixTest {
    @Test
    fun build() {
        val m = matrixOf(
            rowOf(1, 2, 3),
            rowOf(4, 5, 6)
        )

        val x = Matrix(2, 3) { i, j -> j + i * 3 + 1 }

        println(m)
        println(m.transposed)
        println(x)
    }
}