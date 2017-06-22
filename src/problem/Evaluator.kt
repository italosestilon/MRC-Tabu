package problem

import java.util.*

/**
 * Created by estilon on 02/06/17.
 */
interface Evaluator {
    fun problemSize() : Int;
    fun getSet(): Array<LinkedList<Int>>;
    fun getCosts() : Array< IntArray>;
    fun evaluate(sol: Solution): Int;
    fun evaluateExchange(sol: Solution, set: Int, elem: Int): Int;
    fun candidates(): Int
}