package exp

import me.altered.math.Matrix
import me.altered.math.buildMatrix
import org.junit.Test

internal class MatrixTest {
    @Test
    fun build() {
        val mx = buildMatrix(3) {
            append(1, 2, 3, 4, 5, 6)
        }
        val mb = Matrix(2, 3) { i, j ->
            i + j * 3 + 1
        }
        println(mx)
        println(mx[1, 0])
        println(mb)
    }
}