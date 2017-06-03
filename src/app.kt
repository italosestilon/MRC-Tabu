import problem.Evaluator
import problem.MRC
import problem.Solver

/**
 * Created by estilon on 02/06/17.
 */

fun main(args: Array<String>){

    var solver = Solver("", 20)

    val sol = solver.localSearch(2000)

    println(sol)
    println(" cost " + sol.cost)
}
