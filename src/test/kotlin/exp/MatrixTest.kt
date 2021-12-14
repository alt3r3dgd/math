package exp

import me.altered.math.matrixOf
import me.altered.math.rowOf
import org.junit.Test

internal class MatrixTest {
    @Test
    fun build() {
        val m = matrixOf(
            rowOf(1, 2, 3),
            rowOf(4, 5, 6),
            rowOf(7, 8, 9)
        )

        println(m.getComplement(1, 1))
    }
}