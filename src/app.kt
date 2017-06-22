import problem.Evaluator
import problem.MRC
import problem.Solver

/**
 * Created by estilon on 02/06/17.
 */

fun main(args: Array<String>){

    for(i in 8..8) {

        var solver = Solver("intances/instance"+i+".txt", 10)

        val sol = solver.localSearch(20000)

        println(sol)
        println(" cost " + sol.cost)
    }
}
