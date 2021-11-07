package exp

import me.altered.math.Matrix
import me.altered.math.matrixOf
import me.altered.math.rowOf
import org.junit.Test

internal class VectorTest {
    @Test
    fun reassign() {
        val m = matrixOf(rowOf(1))
        println(m.determinant)
    }
}