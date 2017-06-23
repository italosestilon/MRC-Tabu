import problem.Evaluator
import problem.MRC
import problem.Solver

/**
 * Created by estilon on 02/06/17.
 */

fun main(args: Array<String>){

    val filename = args[0]

    val tenure = args[1].toFloat()

    val iterations = args[2].toInt()

    var solver = Solver(filename, tenure, iterations)
    val startTime = System.currentTimeMillis()
    val sol = solver.localSearch(100000)
    val endTime = System.currentTimeMillis()
    val totalTime = endTime - startTime

    print(sol.cost*(-1))
    println(" "+ totalTime.toDouble() / 1000.toDouble())
    println()



}
